package autotesting.practice_2.iteration_2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DepositAccountTest {

    private String adminAuthHeader;
    private String userAuthHeader;
    private int userId;
    private int accountId;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter())
        );
        RestAssured.baseURI = "http://localhost:4111";
        RestAssured.basePath = "/api/v1/accounts";
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

        // Создание счета

        accountId = given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(201)
                .extract()
                .path("id");
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

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(5000.00),
                Arguments.of(4999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanDepositAccountTest(double amount) {
        String requestBody = String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, accountId, amount);

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(200)
                .body("transactions[0].amount", equalTo((float) amount));

        given()
                .header("Authorization", userAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", accountId), equalTo((float) amount))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'DEPOSIT' }.amount", accountId), hasItem((float) amount));
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(5000.01, "Deposit amount cannot exceed 5000"),
                Arguments.of(0, "Deposit amount must be at least 0.01"),
                Arguments.of(-0.01, "Deposit amount must be at least 0.01")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotDepositAccountWithInvalidAmountTest(double amount, String errorMessage) {
        double expectedBalance = 0.00;

        String requestBody = String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, accountId, amount);

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(400)
                .body(equalTo(errorMessage));

        given()
                .header("Authorization", userAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", accountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'DEPOSIT' }", accountId), empty());
    }

    @Test
    public void authorizedUserCannotDepositIntoNonExistingAccountTest() {
        String requestBody = """
                        {
                          "id": -1,
                          "balance": 100
                        }
                        """;

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(403)
                .body(equalTo("Unauthorized access to account"));
    }

    @Test
    public void authorizedUserCannotDepositIntoAnotherUserAccountTest() {
        double expectedBalance = 0.00;
        double amount = 100.00;

        // Создание второго пользователя

        String secondUsername = "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"test$X2p",
                          "role":"USER"
                        }
                        """, secondUsername);

        int secondUserId = given()
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

        // Получение токена второго пользователя

        requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"test$X2p"
                        }
                        """, secondUsername);

        String secondUserAuthHeader = given()
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

        // Создание счета второго пользователя

        int secondAccountId = given()
                .header("Authorization", secondUserAuthHeader)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Пополнение счета второго пользователя первым пользователем

        requestBody = String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, secondAccountId, amount);

        given()
                .header("Authorization", userAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(403)
                .body(equalTo("Unauthorized access to account"));

        given()
                .header("Authorization", secondUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", secondAccountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'DEPOSIT' }", secondAccountId), empty());

        // Удаление второго пользователя

        given()
                .header("Authorization", adminAuthHeader)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/admin")
                .pathParam("id", secondUserId)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    public void unauthorizedUserCannotDepositIntoAccountTest() {
        double expectedBalance = 0.00;
        double amount = 100.00;

        String requestBody = String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, accountId, amount);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(401);

        given()
                .header("Authorization", userAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", accountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'DEPOSIT' }", accountId), empty());
    }

}
