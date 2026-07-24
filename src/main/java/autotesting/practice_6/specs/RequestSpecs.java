package autotesting.practice_6.specs;

import autotesting.practice_6.configs.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    private static final String BASE_URI = Config.getProperty("server") + Config.getProperty("apiVersion");
    private static final String ADMIN_AUTH_TOKEN = "Basic YWRtaW46YWRtaW4=";

    private RequestSpecs() {}

    private static RequestSpecBuilder defaultRequestSpecBuilder () {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
    }

    public static RequestSpecification unauth() {
        return defaultRequestSpecBuilder().build();
    }

    public static RequestSpecification authAsAdmin() {
        return defaultRequestSpecBuilder()
                .addHeader("Authorization", ADMIN_AUTH_TOKEN)
                .build();
    }

    public static RequestSpecification authAsUser(String userAuthToken) {
        return defaultRequestSpecBuilder()
                .addHeader("Authorization", userAuthToken)
                .build();
    }
}
