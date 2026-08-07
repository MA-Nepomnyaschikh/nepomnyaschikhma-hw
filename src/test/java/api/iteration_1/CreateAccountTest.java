package api.iteration_1;

import api.BaseTest;
import models.response.CreateAccountResponseDto;
import models.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;

import java.util.List;

import static testdata.expectedmessages.api.UserApiMessages.CREATE_USER_FORBIDDEN;

@DisplayName("API. Создание счета")
public class CreateAccountTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может создать счет")
    @Test
    @UserSession
    public void authorizedUserCanCreateAccountTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.log("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        StepLogger.log("Проверить создание счета", () -> {
            AccountAssertions.assertAccountCreated(softly, createdAccount);
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), createdAccount.getId());
            softly.assertThat(actualAccount)
                    .usingRecursiveComparison()
                    .isEqualTo(createdAccount);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может создать счет")
    @Test
    @UserSession
    public void unauthorizedUserCannotCreateAccountTest(TestUser user) {

        StepLogger.log("Создать счет", () -> {
            accountSteps.createAccount(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.log("Проверить отсутствие счета", () -> {
            List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(user.getToken());
            softly.assertThat(userAccounts).isEmpty();
        });
    }

    @DisplayName("API. Администратор не может создать счет")
    @Test
    public void adminCannotCreateAccountTest() {
        ErrorResponseDto errorResponse = StepLogger.log("Создать счет администратором", () -> {
            return accountSteps.createAccount(
                    RequestSpecs.authAsAdmin(), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.log("Проверить ошибку при создании счета", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });
    }
}
