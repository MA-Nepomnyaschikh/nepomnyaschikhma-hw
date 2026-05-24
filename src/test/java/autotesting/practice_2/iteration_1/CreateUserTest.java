package autotesting.practice_2.iteration_1;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {

    private final String adminToken = "YWRtaW46YWRtaW4=";
    private final String userToken = "dGVzdHgycDp0ZXN0JFgycHA=";

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                )
        );
        RestAssured.baseURI = "http://localhost:4111";
        RestAssured.basePath = "/api/v1/admin";
    }

    public static Stream<Arguments> userValidDataProvider() {
        return Stream.of(
                Arguments.of("testX2p", "test$X2p", "USER"),
                Arguments.of("adminX2p", "admin$X2p", "ADMIN"),
                Arguments.of("1q-_.", "test$X2p", "USER")
                );
    }

    @MethodSource("userValidDataProvider")
    @ParameterizedTest
    public void adminCanCreateUserWithValidDataTest(String username, String password, String role) {
        String requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"%s",
                          "role":"%s"
                        }
                        """, username, password, role);

        given()
                .header("Authorization", "Basic " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("username", equalTo(username))
                .body("password", notNullValue())
                .body("role", equalTo(role));
    }

    @Test
    public void adminCannotCreateUserWithExistingUsernameTest() {
        String requestBody = """
                        {
                          "username":"testX2p",
                          "password":"test$X2p",
                          "role":"USER"
                        }
                        """;

        given()
                .header("Authorization", "Basic " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(201);

        given()
                .header("Authorization", "Basic " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(400)
                .body(equalTo("Error: Username 'testX2p' already exists."));
    }

    public static Stream<Arguments> userInvalidDataProvider() {
        return Stream.of(
                Arguments.of("  ", "test$X2p", "USER", "username", "Username cannot be blank"),
                Arguments.of("ab", "test$X2p", "USER", "username", "Username must be between 3 and 15 characters"),
                Arguments.of("qwertyuiopasdfgh", "test$X2p", "USER", "username", "Username must be between 3 and 15 characters"),
                Arguments.of("abc1$", "test$X2p", "USER", "username", "Username must contain only letters, digits, dashes, underscores, and dots"),
                Arguments.of("testX2p", "", "USER", "password", "Password cannot be blank"),
                Arguments.of("testX2p", "pass$1R", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "passworD$", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "PASSWORD$1", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "password$1", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "passworD1", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "pass worD1$", "USER", "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of("testX2p", "test$X2p", "TEST", "role", "Role must be either 'ADMIN' or 'USER'")
        );
    }

    @MethodSource("userInvalidDataProvider")
    @ParameterizedTest
    public void adminCannotCreateUserWithInvalidDataTest(String username, String password, String role, String errorKey, String errorValue) {
        String requestBody = String.format("""
                        {
                          "username":"%s",
                          "password":"%s",
                          "role":"%s"
                        }
                        """, username, password, role);

        given()
                .header("Authorization", "Basic " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/users")
        .then()
                .statusCode(400)
                .body(errorKey, contains(errorValue));
    }

    @Test
    public void userWithoutAdminPermissionsCannotCreateUserTest() {
        given()
                .header("Authorization", "Basic " + userToken)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "username":"testX2p",
                          "password":"test$X2p",
                          "role":"USER"
                        }
                        """)
        .when()
                .post("/users")
        .then()
                .statusCode(401);
    }
}
