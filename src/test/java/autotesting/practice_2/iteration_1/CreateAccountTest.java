package autotesting.practice_2.iteration_1;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateAccountTest {

    private final String adminToken = "YWRtaW46YWRtaW4=";

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                )
        );
        RestAssured.baseURI = "http://localhost:4111";
        RestAssured.basePath = "/api/v1/accounts";
    }

    @Test
    public void userCanCreateAccountTest() {

        // Создание пользователя
        given()
                .header("Authorization", "Basic " + adminToken)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/admin")
                .body("""
                        {
                          "username":"testX2p1",
                          "password":"test$X2p",
                          "role":"USER"
                        }
                        """)
        .when()
                .post("/users")
        .then()
                .statusCode(201);

        // Получение токена пользователя

        String userAuthHeader = given()
                .contentType(ContentType.JSON)
                .basePath("/api/v1/auth")
                .body("""
                        {
                          "username":"testX2p1",
                          "password":"test$X2p"
                        }
                        """)
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

        // Создание счета

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(201);

        // Проверка наличия счета у пользователя

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].accountNumber", equalTo("ACC1"));
    }
}
