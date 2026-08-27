package api.iteration_3;

import api.BaseTest;
import models.api.request.CreateUserRequestDto;
import models.api.response.CreateUserResponseDto;
import models.api.response.ErrorResponseDto;
import models.api.response.GetUserResponseDto;
import models.api.response.ValidationErrorResponseDto;
import models.db.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;
import testdata.randommodelgenerator.RandomModelGenerator;

import java.util.List;
import java.util.stream.Stream;

import static testdata.UserData.ADMIN_ROLE;
import static testdata.UserData.USER_ROLE;
import static testdata.UserData.generateUserDto;
import static testdata.UserData.getPassword;
import static testdata.UserData.getUsername;
import static testdata.expectedmessages.api.UserApiMessages.*;

@DisplayName("API. Удаление пользователя")
public class DeleteUserTest extends BaseTest {


    public static Stream<Arguments> validUserDataProvider() {
        return Stream.of(
                Arguments.of("Удаление пользователя без прав администратора", generateUserDto(getUsername(), getPassword(), USER_ROLE)),
                Arguments.of("Удаление пользователя с правами администратора", generateUserDto(getUsername(), getPassword(), ADMIN_ROLE))
        );
    }

    @DisplayName("API. Администратор может удалить пользователя")
    @MethodSource("validUserDataProvider")
    @ParameterizedTest(name= "{0}")
    public void adminCanDeleteUserTest(String testName, CreateUserRequestDto userDto) {
        CreateUserResponseDto createdUser = StepLogger.apiStep("Создать пользователя", () -> {
            return userSteps.createUser(userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.created())
                    .extract().as(CreateUserResponseDto.class);
        });

        String deleteResponse = StepLogger.apiStep("Удалить пользователя", () -> {
            return userSteps.deleteUserById(createdUser.getId())
                    .extract().asString();
        });

        StepLogger.apiStep("Проверить удаление пользователя", () -> {
            softly.assertThat(deleteResponse)
                    .isEqualTo(DELETE_USER_SUCCESSFULLY.formatted(createdUser.getId()));
        });

        StepLogger.apiStep("Проверить отсутствие пользователя в системе через API", () -> {
            List<GetUserResponseDto> actualUsers = userSteps.getAllUsers();
            softly.assertThat(actualUsers)
                    .filteredOn(user -> user.getId() == createdUser.getId())
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить отсутствие пользователя в системе через БД", () -> {
            List<Customer> actualUsersFromDb = databaseSteps.getAllCustomers();
            softly.assertThat(actualUsersFromDb)
                    .filteredOn(customer -> customer.getId() == createdUser.getId())
                    .isEmpty();
        });
    }

    @DisplayName("API. Администратор не может удалить пользователя через невалидный id")
    @Test
    public void adminCannotDeleteUserWithInvalidIdTest() {
        long invalidId = -1L;

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Удалить пользователя", () -> {
            return userSteps.deleteUserById(invalidId, RequestSpecs.authAsAdmin(), ResponseSpecs.notFound())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при удалении пользователя", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(DELETE_USER_INVALID_ID.formatted(invalidId));
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может удалить пользователя")
    @Test
    public void adminCannotDeleteUserWithoutAuthTest() {
        CreateUserResponseDto createdUser = StepLogger.apiStep("Создать пользователя", () -> {
            CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
            return userSteps.createUser(userDto);
        });

        StepLogger.apiStep("Удалить пользователя", () -> {
            userSteps.deleteUserById(createdUser.getId(), RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить наличие пользователя в системе через API", () -> {
            List<GetUserResponseDto> actualUsers = userSteps.getAllUsers();
            softly.assertThat(actualUsers)
                    .filteredOn(user -> user.getId() == createdUser.getId())
                    .singleElement();
        });

        StepLogger.apiStep("Проверить отсутствие пользователя в системе через БД", () -> {
            List<Customer> actualUsersFromDb = databaseSteps.getAllCustomers();
            softly.assertThat(actualUsersFromDb)
                    .filteredOn(customer -> customer.getId() == createdUser.getId())
                    .singleElement();
        });
    }

    @DisplayName("API. Пользователь без прав администратора не может удалить пользователя")
    @Test
    @UserSession
    public void userWithoutAdminPermissionsCannotDeleteUserTest(TestUser userWithoutAdminPermissions) {
        CreateUserResponseDto createdUser = StepLogger.apiStep("Создать пользователя для удаления", () -> {
            CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
            return userSteps.createUser(userDto);
        });

        ErrorResponseDto errorResponse = StepLogger.apiStep("Удалить пользователя", () -> {
            return userSteps.deleteUserById(
                    createdUser.getId(), RequestSpecs.authAsUser(userWithoutAdminPermissions.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при удалении пользователя", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });

        StepLogger.apiStep("Проверить наличие пользователя в системе через API", () -> {
            List<GetUserResponseDto> actualUsers = userSteps.getAllUsers();
            softly.assertThat(actualUsers)
                    .filteredOn(user -> user.getId() == createdUser.getId())
                    .singleElement();
        });

        StepLogger.apiStep("Проверить отсутствие пользователя в системе через БД", () -> {
            List<Customer> actualUsersFromDb = databaseSteps.getAllCustomers();
            softly.assertThat(actualUsersFromDb)
                    .filteredOn(customer -> customer.getId() == createdUser.getId())
                    .singleElement();
        });
    }
}
