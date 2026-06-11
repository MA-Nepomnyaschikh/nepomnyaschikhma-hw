package autotesting.practice_3.iteration_1;

import autotesting.practice_3.generators.TestData;
import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.request.CreateUserRequestDto;
import autotesting.practice_3.contract.models.response.CreateUserResponseDto;
import autotesting.practice_3.contract.models.response.ErrorResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CreateUserTest extends BaseTest {

    public static Stream<Arguments> userValidDataProvider() {
        return Stream.of(
                Arguments.of(TestData.getUsername(), TestData.getPassword(), UserRole.USER),
                Arguments.of(TestData.getUsername(), TestData.getPassword(), UserRole.ADMIN)
                );
    }

    @MethodSource("userValidDataProvider")
    @ParameterizedTest
    public void adminCanCreateUserWithValidDataTest(String username, String password, UserRole role) {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        CreateUserResponseDto createUserResponseDto = new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(createUserRequestDto)
                .extract()
                .as(CreateUserResponseDto.class);

        softly.assertThat(createUserResponseDto.getUsername()).isEqualTo(username);
        softly.assertThat(createUserResponseDto.getPassword()).isNotEqualTo(password);
        softly.assertThat(createUserResponseDto.getRole()).isEqualTo(role);
    }

    @Test
    public void adminCannotCreateUserWithExistingUsernameTest() {
        String username = TestData.getUsername();

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(username)
                .password(TestData.getPassword())
                .role(UserRole.USER)
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(createUserRequestDto);

        CreateUserRequestDto duplicateUsernameRequestDto = CreateUserRequestDto.builder()
                .username(username)
                .password(TestData.getPassword())
                .role(UserRole.USER)
                .build();

        String errorResponse = new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.badRequest())
                .post(duplicateUsernameRequestDto)
                .extract().asString();

        Assertions.assertThat(errorResponse).isEqualTo("Error: Username '" + username + "' already exists.");
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
    public void adminCannotCreateUserWithInvalidDataTest(String username, String password, UserRole role, String errorKey, String errorValue) {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        ErrorResponseDto errorResponseDto = new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.badRequest())
                .post(createUserRequestDto)
                .extract().as(ErrorResponseDto.class);

        softly.assertThat(errorResponseDto.getErrorKey()).isEqualTo(errorKey);
        softly.assertThat(errorResponseDto.getErrorValue()).isEqualTo(errorValue);
    }

    @Test
    public void userWithoutAdminPermissionsCannotCreateUserTest() {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
                .build();

        new CreateUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(createUserRequestDto);
    }
}
