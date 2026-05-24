package autotesting.practice_2.iteration_2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateCustomerProfileTest {

    private String adminAuthHeader;
    private String userAuthHeader;
    private int userId;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter())
        );
        RestAssured.baseURI = "http://localhost:4111";
        RestAssured.basePath = "/api/v1/customer";
    }

    @BeforeEach
    public void createTestData() {
        //Авторизация под админом

        adminAuthHeader = given()
                .contentType(ContentType.JSON)
                .basePath("/api/v1/auth")
                .body("""
                        {
                          "username":"admin",
                          "password":"admin"
                        }
                        """)
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

        // Создание пользователя

        String username = "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"test$X2p",
                          "role":"USER"
                        }
                        """, username);

        userId = given()
                .header("Authorization", adminAuthHeader)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/admin")
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Получение токена пользователя

        requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"test$X2p"
                        }
                        """, username);

        userAuthHeader = given()
                .contentType(ContentType.JSON)
                .basePath("/api/v1/auth")
                .body(requestBody)
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

    }

    @AfterEach
    public void deleteTestData() {
        // Удаление пользователя

        given()
                .header("Authorization", adminAuthHeader)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/admin")
                .pathParam("id", userId)
        .when()
                .delete("/users/{id}")
        .then()
                .statusCode(200);
    }

    @Test
    public void authorizedUserCanSetValidName() {
        String name = "Mikhail Nepomnyaschikh";

        String requestBody = String.format("""
                        {
                          "name": "%s"
                        }
                        """, name);

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .put("/profile")
        .then()
                .statusCode(200)
                .body("customer.name", equalTo(name))
                .body("message", equalTo("Profile updated successfully"));
    }

    public static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of("Mikhail"),
                Arguments.of("Nepomnyaschikh Mikhail Aleksandrovich"),
                Arguments.of("Mikhail Nepomnyaschikh1"),
                Arguments.of("Mikhail! Nepomnyaschikh"),
                Arguments.of("Mikhail  Nepomnyaschikh"),
                Arguments.of("Mikhail Nepomnyaschikh "),
                Arguments.of(" Mikhail")
        );
    }

    @MethodSource("invalidNameProvider")
    @ParameterizedTest
    public void authorizedUserCannotSetInvalidName(String name) {
        String requestBody = String.format("""
                        {
                          "name": "%s"
                        }
                        """, name);

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .put("/profile")
        .then()
                .statusCode(400)
                .body(equalTo("Name must contain two words with letters only"));
    }

    @Test
    public void unauthorizedUserCannotChangeName() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Mikhail Nepomnyaschikh"
                        }
                        """)
        .when()
                .put("/profile")
        .then()
                .statusCode(401);
    }
}
