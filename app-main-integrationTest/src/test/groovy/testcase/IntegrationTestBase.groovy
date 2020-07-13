/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.Application
import com.github.fj.board.endpoint.AbstractResponseDto
import com.github.fj.board.endpoint.ErrorResponseDto
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet
import spock.lang.Specification
import test.com.github.fj.board.appconfig.TestConfigurations

import javax.annotation.Nonnull
import javax.annotation.Nullable

import static io.restassured.RestAssured.given
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [
        Application.class,
        TestConfigurations.class
])
class IntegrationTestBase extends Specification {
    private static final Set<String> DECLARED_DOCUMENT_IDS = new HashSet()
    private static final String DEFAULT_HOST = "localhost"
    private static final int DEFAULT_PORT = 8000

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation()

    @LocalServerPort
    private int port

    @Autowired
    private ObjectMapper defaultObjMapper

    private RequestSpecification documentationSpec

    def setup() {
        // There'll be groovyc error without this explicit local reference declaration
        final mapper = defaultObjMapper

        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build()
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory { cls, charset -> mapper }
        )
    }

    final RequestSpecification jsonRequestSpec(final @Nonnull String documentId) {
        return jsonRequestSpec(documentId, null, null)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull RequestFieldsSnippet reqDoc
    ) {
        return jsonRequestSpec(documentId, reqDoc, null)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull ResponseFieldsSnippet respDoc
    ) {
        return jsonRequestSpec(documentId, null, respDoc)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nullable RequestFieldsSnippet reqDoc,
            final @Nullable ResponseFieldsSnippet respDoc
    ) {
        if (DECLARED_DOCUMENT_IDS.contains(documentId)) {
            throw new IllegalArgumentException("Document id '$documentId' is already declared.")
        } else {
            DECLARED_DOCUMENT_IDS.add(documentId)
        }

        final List<Snippet> snippets = new ArrayList()
        if (reqDoc != null) {
            snippets.add(reqDoc)
        }
        if (respDoc != null) {
            snippets.add(respDoc)
        }
        final Snippet[] snippetsArray = snippets.toArray(new Snippet[snippets.size()]) as Snippet[]

        final baseReqSpec = given(this.documentationSpec).log().all()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString())

        if (documentId.isEmpty()) {
            return baseReqSpec
        } else {
            def uriProcessor = modifyUris().host(DEFAULT_HOST)
            if (DEFAULT_PORT == 0) {
                uriProcessor.removePort()
            } else {
                uriProcessor.port(DEFAULT_PORT)
            }

            return baseReqSpec.filter(document(documentId,
                    preprocessRequest(prettyPrint(), uriProcessor),
                    preprocessResponse(prettyPrint()),
                    snippetsArray
            ))
        }
    }

    final <T> T expectResponse(
            final @Nonnull ValidatableResponse respSpec,
            final @Nonnull Class<T> klass
    ) {
        final responseDto = parseResponse(respSpec)
        final body = responseDto.body

        return defaultObjMapper.convertValue(body, klass)
    }

    final ErrorResponseDto expectError(final @Nonnull ValidatableResponse respSpec) {
        final rawResponseDto = parseResponse(respSpec)

        return new ErrorResponseDto(
                defaultObjMapper.convertValue(rawResponseDto.body, ErrorResponseDto.Body.class)
        )
    }

    final ObjectMapper getJsonMapper() {
        return defaultObjMapper
    }

    private final AbstractResponseDto<Object> parseResponse(final @Nonnull ValidatableResponse respSpec) {
        final jsonResponse = respSpec.extract().body().asString()
        final Map<String, Object> map = defaultObjMapper.readValue(jsonResponse, Map.class)

        final String strType = map.get("type")
        final Object jsonBody = map.get("body")

        final AbstractResponseDto.Type type = AbstractResponseDto.Type.valueOf(strType)

        return new AbstractResponseDto(type) {
            @Override
            final Object getBody() { return jsonBody }

            @Override
            String toString() {
                return "AbstractResponseDto<Object>(type = '$strType', body = $jsonBody)"
            }
        }
    }

    static List<FieldDescriptor> baseFieldDescriptors() {
        return [
                fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description(AbstractResponseDto.DESC_TYPE),
                fieldWithPath("timestamp")
                        .type(JsonFieldType.NUMBER)
                        .description(AbstractResponseDto.DESC_TIMESTAMP),
                fieldWithPath("body")
                        .type(JsonFieldType.OBJECT)
                        .description(AbstractResponseDto.DESC_BODY),
        ]
    }

    /**
     * Since all API error are in same format({@link ErrorResponseDto}), this method could be handy for documenting error cases.
     */
    static ResponseFieldsSnippet getErrorResponseFields() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.message")
                        .type(JsonFieldType.STRING)
                        .description(ErrorResponseDto.DESC_BODY_MESSAGE),
                fieldWithPath("body.cause")
                        .type(JsonFieldType.STRING)
                        .description(ErrorResponseDto.DESC_BODY_CAUSE)
        ]

        return responseFields(baseFieldDescriptors() + fields)
    }
}
