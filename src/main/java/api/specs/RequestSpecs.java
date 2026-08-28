package api.specs;

import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import common.configs.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;

public class RequestSpecs {

    private static final String BASE_URI = Config.getProperty("apiBaseUrl");
    private static final String ADMIN_TOKEN = Config.getProperty("admin.token");

    private RequestSpecs() {}

    private static RequestSpecBuilder defaultRequestSpecBuilder () {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .addFilter(new SwaggerCoverageRestAssured(new FileSystemOutputWriter(Paths.get("target/" + OUTPUT_DIRECTORY))));
    }

    public static RequestSpecification unauth() {
        return defaultRequestSpecBuilder().build();
    }

    public static RequestSpecification authAsAdmin() {
        return defaultRequestSpecBuilder()
                .addHeader("Authorization", ADMIN_TOKEN)
                .build();
    }

    public static RequestSpecification authAsUser(String userAuthToken) {
        return defaultRequestSpecBuilder()
                .addHeader("Authorization", userAuthToken)
                .build();
    }
}
