package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecs {
    public static final String AUTH_HEADER = "Authorization";

    private ResponseSpecs() {}

    private static ResponseSpecBuilder defaultResponseSpecBuilder() {
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification ok() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification created() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(201)
                .build();
    }

    public static ResponseSpecification badRequest() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }

    public static ResponseSpecification unauthorized() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(401)
                .build();
    }

    public static ResponseSpecification forbidden() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(403)
                .build();
    }

    public static ResponseSpecification notFound() {
        return defaultResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
    }
}
