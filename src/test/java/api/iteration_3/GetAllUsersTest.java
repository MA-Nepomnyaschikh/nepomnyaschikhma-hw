package api.iteration_3;

import api.BaseTest;
import models.response.CreateUserResponseDto;
import models.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;

import java.util.List;

import static testdata.expectedmessages.api.UserApiMessages.GET_USERS_LIST_FORBIDDEN;

public class GetAllUsersTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь с правами администратора может получить список пользователей")
    @Test
    @UserSession
    public void authorizedUserWithAdminPermissionsCanGetUsersListTest(TestUser user) {
        List<CreateUserResponseDto> actualUsersList = StepLogger.log("Получить список пользователей", () -> {
            return userSteps.getAllUsers();
        });

        List<CreateUserResponseDto> expectedUserList = List.of(user.getResponseDto());

        StepLogger.log("Проверить полученный список пользователей", () -> {
            softly.assertThat(actualUsersList).isEqualTo(expectedUserList);
        });
    }

    @DisplayName("API. Авторизованный пользователь без прав администратора не может получить список пользователей")
    @Test
    @UserSession
    public void authorizedUserWithoutAdminPermissionsCannotGetUsersListTest(TestUser user) {
        ErrorResponseDto errorResponse = StepLogger.log("Получить список пользователей", () -> {
            return userSteps.getAllUsers(RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.log("Проверить ошибку при получении списка пользователей", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(GET_USERS_LIST_FORBIDDEN);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить список пользователей")
    @Test
    @UserSession
    public void unauthorizedUserCannotGetUsersListTest(TestUser user) {
        StepLogger.log("Получить список пользователей без авторизации", () -> {
            userSteps.getAllUsers(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }
}
