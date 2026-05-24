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
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TransferFundsTest {

    private String adminAuthHeader;
    private String firstUserAuthHeader;
    private String secondUserAuthHeader;
    private int firstUserId;
    private int secondUserId;
    private int firstUserFirstAccountId;
    private int firstUserSecondAccountId;
    private int secondUserFirstAccountId;

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

        // Создание первого пользователя

        String username = "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String requestBody = String.format("""
                {
                  "username":"%s",
                  "password":"test$X2p",
                  "role":"USER"
                }
                """, username);

        firstUserId = given()
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

        // Получение токена первого пользователя

        requestBody = String.format("""
                {
                  "username":"%s",
                  "password":"test$X2p"
                }
                """, username);

        firstUserAuthHeader = given()
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

        // Создание первого счета первого пользователя

        firstUserFirstAccountId = given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Пополнение первого счета первого пользователя

        requestBody = String.format(Locale.US, """
                        {
                          "id": %d,
                          "balance": 5000.00
                        }
                        """, firstUserFirstAccountId);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(200);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/deposit")
        .then()
                .statusCode(200);

        // Создание второго счета первого пользователя

        firstUserSecondAccountId = given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Создание второго пользователя

        String secondUsername = "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        requestBody = String.format("""
                {
                  "username":"%s",
                  "password":"test$X2p",
                  "role":"USER"
                }
                """, secondUsername);

        secondUserId = given()
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

        secondUserAuthHeader = given()
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

        secondUserFirstAccountId = given()
                .header("Authorization", secondUserAuthHeader)
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
        // Удаление пользователей

        given()
                .header("Authorization", adminAuthHeader)
                .contentType(ContentType.JSON)
                .basePath("/api/v1/admin")
                .pathParam("id", firstUserId)
        .when()
                .delete("/users/{id}")
        .then()
                .statusCode(200);

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

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(10000.00),
                Arguments.of(9999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountBetweenTheirAccountsTest(double amount) {

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, firstUserSecondAccountId, amount);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(200)
                .body("senderAccountId", equalTo(firstUserFirstAccountId))
                .body("receiverAccountId", equalTo(firstUserSecondAccountId))
                .body("message", equalTo("Transfer successful"))
                .body("amount", equalTo((float) amount));

        // Проверка наличия пополнения

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", firstUserSecondAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("amount", hasItem((float) amount))
                .body("type", hasItem("TRANSFER_IN"));
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double amount) {
        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, secondUserFirstAccountId, amount);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(200)
                .body("senderAccountId", equalTo(firstUserFirstAccountId))
                .body("receiverAccountId", equalTo(secondUserFirstAccountId))
                .body("message", equalTo("Transfer successful"))
                .body("amount", equalTo((float) amount));

        // Проверка наличия пополнения

        given()
                .header("Authorization", secondUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", secondUserFirstAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("amount", hasItem((float) amount))
                .body("type", hasItem("TRANSFER_IN"));
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(10000.01, "Transfer amount cannot exceed 10000"),
                Arguments.of(0, "Transfer amount must be at least 0.01"),
                Arguments.of(-0.01, "Transfer amount must be at least 0.01")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountBetweenTheirAccountsTest(double amount, String errorMessage) {

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, firstUserSecondAccountId, amount);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(400)
                .body(equalTo(errorMessage));

        // Проверка отсутствия пополнения

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", firstUserSecondAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(double amount, String errorMessage) {

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, secondUserFirstAccountId, amount);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(400)
                .body(equalTo(errorMessage));

        // Проверка отсутствия пополнения

        given()
                .header("Authorization", secondUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", secondUserFirstAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    @Test
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest() {

        // Перевод для снижения баланса

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 5000.00
                }
                """, firstUserFirstAccountId, secondUserFirstAccountId);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(200);

        // Перевод на сумму, больше чем баланс

        requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 5000.01
                }
                """, firstUserFirstAccountId, firstUserSecondAccountId);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(400)
                .body(equalTo("Invalid transfer: insufficient funds or invalid accounts"));

        // Проверка отсутствия пополнения

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", firstUserSecondAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("", hasSize(0));
    }


    @Test
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest() {
        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 1000
                }
                """, firstUserFirstAccountId, -1);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(400)
                .body(equalTo("Invalid transfer: insufficient funds or invalid accounts"));
    }

    @Test
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest() {
        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 1000
                }
                """, -1, firstUserSecondAccountId);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(403)
                .body(equalTo("Unauthorized access to account"));
    }

    @Test
    public void unauthorizedUserCannotTransferFundsIntoAnotherAccountTest() {
        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 1000
                }
                """, firstUserFirstAccountId, firstUserSecondAccountId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(401);

        // Проверка отсутствия пополнения

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", firstUserSecondAccountId)
        .when()
                .get("{id}/transactions")
        .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

}
