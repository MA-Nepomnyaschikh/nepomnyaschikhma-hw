package api.iteration_3;

import api.BaseTest;
import models.api.response.ErrorResponseDto;
import models.api.response.GetUserResponseDto;
import models.db.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.comparisons.UserComparisonFields;
import supports.context.TestUser;

import java.util.List;

import static testdata.expectedmessages.api.UserApiMessages.GET_USERS_LIST_FORBIDDEN;

@DisplayName("API. Получение списка пользователей")
public class GetAllUsersTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь с правами администратора может получить список пользователей")
    @Test
    @UserSession
    public void authorizedUserWithAdminPermissionsCanGetUsersListTest(TestUser user) {
        List<GetUserResponseDto> actualUsersList = StepLogger.apiStep("Получить список пользователей", () -> {
            return userSteps.getAllUsers();
        });

        StepLogger.apiStep("Проверить список пользователей через API", () -> {
        softly.assertThat(actualUsersList)
                .anySatisfy(actualUser -> {
                    softly.assertThat(actualUser.getId()).isEqualTo(user.getId());
                    softly.assertThat(actualUser.getUsername()).isEqualTo(user.getUsername());
                });
        });

        StepLogger.apiStep("Проверить список пользователей через БД", () -> {
            List<Customer> expectedUsersListFromDb = databaseSteps.getAllCustomers();

            softly.assertThat(expectedUsersListFromDb)
                    .usingRecursiveFieldByFieldElementComparatorOnFields(UserComparisonFields.SELECT_USER_RESPONSE_TO_GET_USER_RESPONSE.fields())
                    .isEqualTo(actualUsersList);
        });
    }

    @DisplayName("API. Авторизованный пользователь без прав администратора не может получить список пользователей")
    @Test
    @UserSession
    public void authorizedUserWithoutAdminPermissionsCannotGetUsersListTest(TestUser user) {
        ErrorResponseDto errorResponse = StepLogger.apiStep("Получить список пользователей", () -> {
            return userSteps.getAllUsers(RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при получении списка пользователей", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(GET_USERS_LIST_FORBIDDEN);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить список пользователей")
    @Test
    @UserSession
    public void unauthorizedUserCannotGetUsersListTest(TestUser user) {
        StepLogger.apiStep("Получить список пользователей без авторизации", () -> {
            userSteps.getAllUsers(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }
}
