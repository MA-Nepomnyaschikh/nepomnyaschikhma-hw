package autotesting.practice_2.iteration_1;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class LoginUserTest {

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter())
        );
        RestAssured.baseURI = "http://localhost:4111";
        RestAssured.basePath = "/api/v1/auth";
    }

    @Test
    public void adminCanGenerateAuthTokenTest() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"admin\",\"password\":\"admin\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .header("Authorization", "Basic YWRtaW46YWRtaW4=");

    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        given()
            .contentType(ContentType.JSON)
            .basePath("/api/v1/admin")
            .body("{\"username\":\"testX2p\",\"password\":\"test$X2p\",\"role\":\"USER\"}")
        .when()
            .post("/users")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"testX2p\",\"password\":\"test$X2p\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .header("Authorization", "Basic dGVzdFgycDp0ZXN0JFgycA==");

    }
}
