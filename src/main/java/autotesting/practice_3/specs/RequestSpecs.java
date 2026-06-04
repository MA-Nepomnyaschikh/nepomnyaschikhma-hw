package autotesting.practice_3.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    private static final String BASE_URI = "http://localhost:4111";
    private static final String ADMIN_AUTH_HEADER = "Basic YWRtaW46YWRtaW4=";

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
                .addHeader("Authorization", ADMIN_AUTH_HEADER)
                .build();
    }

    public static RequestSpecification authAsUser(String userAuthHeader) {
        return defaultRequestSpecBuilder()
                .addHeader("Authorization", userAuthHeader)
                .build();
    }
}
