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
        double expectedBalance = 10000.00 - amount;

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

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                hasItem((float) amount))
                .body(String.format("find { it.id == %d }.balance", firstUserSecondAccountId), equalTo((float) amount))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                firstUserSecondAccountId),
                                hasItem((float) amount));
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double amount) {
        double expectedBalance = 10000.00 - amount;

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

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                hasItem((float) amount));

        given()
                .header("Authorization", secondUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", secondUserFirstAccountId), equalTo((float) amount))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                secondUserFirstAccountId),
                                hasItem((float) amount));
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(10000.01, "Transfer amount cannot exceed 10000"),
                Arguments.of(0.00, "Transfer amount must be at least 0.01"),
                Arguments.of(-0.01, "Transfer amount must be at least 0.01")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountBetweenTheirAccountsTest(double amount, String errorMessage) {
        double expectedBalanceFirstAcc = 10000.00;
        double expectedBalanceSecondAcc = 0.00;

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

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalanceFirstAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                    firstUserFirstAccountId),
                                    not(hasItem((float) amount)))
                .body(String.format("find { it.id == %d }.balance", firstUserSecondAccountId), equalTo((float) expectedBalanceSecondAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                    firstUserSecondAccountId),
                                    not(hasItem((float) amount)));
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(double amount, String errorMessage) {
        double expectedBalanceFirstUserAcc = 10000.00;
        double expectedBalanceSecondUserAcc = 0.00;

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

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
                .when()
                .get("/accounts")
                .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalanceFirstUserAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                not(hasItem((float) amount)));

        given()
                .header("Authorization", secondUserAuthHeader)
                .basePath("/api/v1/customer")
                .when()
                .get("/accounts")
                .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", secondUserFirstAccountId), equalTo((float) expectedBalanceSecondUserAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                secondUserFirstAccountId),
                                not(hasItem((float) amount)));
    }

    @Test
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest() {
        double amount = 5000.01;
        double expectedBalanceFirstAcc = 5000.00;
        double expectedBalanceSecondAcc = 0.00;

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
                .body(equalTo("Invalid transfer: insufficient funds or invalid accounts"));

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalanceFirstAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                not(hasItem((float) amount)))
                .body(String.format("find { it.id == %d }.balance", firstUserSecondAccountId), equalTo((float) expectedBalanceSecondAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                firstUserSecondAccountId),
                                not(hasItem((float) amount)));
    }


    @Test
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest() {
        double expectedBalance = 10000.00;
        double amount = 1000.00;

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, -1, amount);

        given()
                .header("Authorization", firstUserAuthHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/transfer")
        .then()
                .statusCode(400)
                .body(equalTo("Invalid transfer: insufficient funds or invalid accounts"));

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                not(hasItem((float) amount)));
    }

    @Test
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest() {
        double expectedBalance = 0.00;
        double amount = 1000.00;

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": 1000.00
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

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserSecondAccountId), equalTo((float) expectedBalance))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                firstUserSecondAccountId),
                                not(hasItem((float) amount)));
    }

    @Test
    public void unauthorizedUserCannotTransferFundsIntoAnotherAccountTest() {
        double expectedBalanceFirstAcc = 10000.00;
        double expectedBalanceSecondAcc = 0.00;
        double amount = 1000.00;

        String requestBody = String.format(Locale.US, """
                {
                  "senderAccountId": %d,
                  "receiverAccountId": %d,
                  "amount": %.2f
                }
                """, firstUserFirstAccountId, firstUserSecondAccountId, amount);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/transfer")
                .then()
                .statusCode(401);

        given()
                .header("Authorization", firstUserAuthHeader)
                .basePath("/api/v1/customer")
                .when()
                .get("/accounts")
                .then()
                .statusCode(200)
                .body(String.format("find { it.id == %d }.balance", firstUserFirstAccountId), equalTo((float) expectedBalanceFirstAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_OUT' }.amount",
                                firstUserFirstAccountId),
                                not(hasItem((float) amount)))
                .body(String.format("find { it.id == %d }.balance", firstUserSecondAccountId), equalTo((float) expectedBalanceSecondAcc))
                .body(String.format("find { it.id == %d }.transactions.findAll { it.type == 'TRANSFER_IN' }.amount",
                                firstUserSecondAccountId),
                                not(hasItem((float) amount)));
    }

}
