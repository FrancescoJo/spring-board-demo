/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.Application
import com.github.fj.board.endpoint.AbstractResponseDto
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
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet
import spock.lang.Specification
import test.com.github.fj.board.appconfig.TestConfigurations

import javax.annotation.Nonnull

import static io.restassured.RestAssured.given
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
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
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build()
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory { cls, charset -> defaultObjMapper }
        )
    }

    protected final RequestSpecification jsonRequestSpec(final @Nonnull String documentId) {
        return jsonRequestSpec(documentId, null, null)
    }

    protected final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull RequestFieldsSnippet reqDoc
    ) {
        return jsonRequestSpec(documentId, reqDoc, null)
    }

    protected final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull ResponseFieldsSnippet respDoc
    ) {
        return jsonRequestSpec(documentId, null, respDoc)
    }

    protected final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull RequestFieldsSnippet reqDoc,
            final @Nonnull ResponseFieldsSnippet respDoc
    ) {
        // TODO: [CONFIRMATION REQUIRED] check duplicated documentId if it is not empty
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

    protected final AbstractResponseDto<Object> expectResponse(
            final @Nonnull ValidatableResponse respSpec
    ) {
        final jsonResponse = respSpec.extract().body().asString()
        final Map<String, Object> map = defaultObjMapper.readValue(jsonResponse, Map.class)

        final String strType = map.get("type")
        final Object jsonBody = map.get("body")

        final AbstractResponseDto.Type type = AbstractResponseDto.Type.valueOf(strType)

        return new AbstractResponseDto(type) {
            @Override
            final Object getBody() { return jsonBody }
        }
    }

    protected final <T> T extractResponse(
            final @Nonnull AbstractResponseDto<Object> rawResponse,
            final @Nonnull Class<T> klass
    ) {
        final body = rawResponse.body

        return defaultObjMapper.convertValue(body, klass)
    }
}
