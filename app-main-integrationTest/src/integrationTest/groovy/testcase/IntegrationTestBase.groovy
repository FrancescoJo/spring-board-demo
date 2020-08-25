/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.Application
import com.github.fj.board.endpoint.AbstractResponseDto
import com.github.fj.board.endpoint.ErrorResponseDto
import com.github.fj.board.endpoint.common.response.PageableResponse
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.vo.auth.UserAgent
import de.skuzzle.semantic.Version
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.Header
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
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
import java.util.stream.Collectors

import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.is
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
    static Version TEST_CLIENT_VERSION = Version.parseVersion("1.0.0")

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

    final RequestSpecification jsonRequestSpec() {
        return jsonRequestSpec("")
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
            if (!documentId.isEmpty()) {
                DECLARED_DOCUMENT_IDS.add(documentId)
            }
        }

        final List<Snippet> snippets = new ArrayList()
        if (reqDoc != null) {
            snippets.add(reqDoc)
        }
        if (respDoc != null) {
            snippets.add(respDoc)
        }
        final Snippet[] snippetsArray = snippets.toArray(new Snippet[snippets.size()]) as Snippet[]

        final String platformInfo = "'${System.getProperty("os.name")}' " +
                "${System.getProperty("os.version")} " +
                "${System.getProperty("os.arch")}"

        final baseReqSpec = given(this.documentationSpec).log().all()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString())
                .header(new Header(UserAgent.HEADER_NAME,
                        "${PlatformType.WEB.userAgentName}; $TEST_CLIENT_VERSION; $platformInfo"))

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

    final <T> T expectGenericResponse(
            final @Nonnull Response response,
            final @Nonnull HttpStatus status,
            final @Nonnull Class<T> klass
    ) {
        final responseDto = parseResponse(response.then().assertThat().statusCode(is(status.value())))
        final body = responseDto.body

        return klass.cast(body)
    }

    final <T> PageableResponse<T> expectPageableResponse(
            final @Nonnull Response response,
            final @Nonnull HttpStatus status,
            final @Nonnull Class<T> dataClass
    ) {
        final responseDto = parseResponse(response.then().assertThat().statusCode(is(status.value())))
        final body = responseDto.body

        final page = body["page"] as Integer
        final size = body["size"] as Integer
        final totalCount = body["totalCount"] as Long
        final data = body["data"] as List<Map>

        // Fixing groovyc error: reference problem in closures
        final objMapper = defaultObjMapper

        final List<T> parsedData = data.stream().map {
            objMapper.convertValue(it, dataClass) as T
        }.collect(Collectors.toList())

        return new PageableResponse<T>(
                /* page */       page,
                /* size */       size,
                /* totalCount */ totalCount,
                /* data */       parsedData
        )
    }

    final <T> T expectResponse(
            final @Nonnull Response response,
            final @Nonnull HttpStatus status,
            final @Nonnull Class<T> klass
    ) {
        final responseDto = parseResponse(response.then().assertThat().statusCode(is(status.value())))
        final body = responseDto.body

        return defaultObjMapper.convertValue(body, klass)
    }

    final ErrorResponseDto.Body expectError(
            final @Nonnull Response response,
            final @Nonnull HttpStatus status
    ) {
        final rawResponseDto = parseResponse(response.then().assertThat().statusCode(is(status.value())))

        return new ErrorResponseDto(
                defaultObjMapper.convertValue(rawResponseDto.body, ErrorResponseDto.Body.class)
        ).body
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

    protected static List<FieldDescriptor> basicFieldsDoc() {
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
    static ResponseFieldsSnippet errorResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.message")
                        .type(JsonFieldType.STRING)
                        .description(ErrorResponseDto.DESC_BODY_MESSAGE),
                fieldWithPath("body.cause")
                        .type(JsonFieldType.STRING)
                        .description(ErrorResponseDto.DESC_BODY_CAUSE)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }

    protected static ResponseFieldsSnippet genericBooleanResponseDoc() {
        return responseFields(
                fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description(AbstractResponseDto.DESC_TYPE),
                fieldWithPath("timestamp")
                        .type(JsonFieldType.NUMBER)
                        .description(AbstractResponseDto.DESC_TIMESTAMP),
                fieldWithPath("body")
                        .type(JsonFieldType.BOOLEAN)
                        .description(AbstractResponseDto.DESC_BODY)
        )
    }

    protected static ResponseFieldsSnippet pageableResponseDoc() {
        return pageableResponseDoc(Collections.emptyList())
    }

    protected static ResponseFieldsSnippet pageableResponseDoc(final List<FieldDescriptor> dataFields) {
        return responseFields(basicFieldsDoc() + pageableResponseFields("body", dataFields))
    }

    protected static List<FieldDescriptor> pageableResponseFields(final String prefix, final List<FieldDescriptor> dataFields) {
        return [
                fieldWithPath("${prefix}.page")
                        .type(JsonFieldType.NUMBER)
                        .description(PageableResponse.DESC_PAGE),
                fieldWithPath("${prefix}.size")
                        .type(JsonFieldType.NUMBER)
                        .description(PageableResponse.DESC_SIZE),
                fieldWithPath("${prefix}.totalCount")
                        .type(JsonFieldType.NUMBER)
                        .description(PageableResponse.DESC_TOTAL_COUNT),
                fieldWithPath("${prefix}.data[]")
                        .type(JsonFieldType.ARRAY)
                        .description(PageableResponse.DESC_DATA)
        ] + dataFields
    }
}
