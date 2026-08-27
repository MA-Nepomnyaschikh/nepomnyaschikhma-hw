package ui.iteration_1;

import models.api.response.CreateAccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.UserDashboardPage;
import supports.StepLogger;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;
import ui.BaseUiTest;

import java.util.List;

import static testdata.expectedmessages.ui.AccountUiMessages.ACCOUNT_CREATED_SUCCESSFULLY;

@DisplayName("UI. Создание счета")
public class CreateAccountTest extends BaseUiTest {

    @DisplayName("UI. Пользователь может создать аккаунт")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCanCreateAccountTest(TestUser user) {
        UserDashboardPage userDashboard = new UserDashboardPage();

        String alertMessage = StepLogger.uiStep("Создать аккаунт ", () -> {
            return userDashboard
                    .open()
                    .shouldBeOpened()
                    .createAccount()
                    .getAlertMessageAndAccept();

        });

        String accountNumber = userDashboard.extractAccountNumber(alertMessage);

        StepLogger.uiStep("Проверить создание аккаунта через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(ACCOUNT_CREATED_SUCCESSFULLY.formatted(accountNumber));
        });

        StepLogger.apiStep("Проверить создание аккаунта через API", () -> {
            List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(user.getToken());
            softly.assertThat(userAccounts)
                    .singleElement()
                    .satisfies(account -> {
                        AccountAssertions.assertCreateAccountResponse(softly, account);
                        softly.assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
                    });
        });
    }
}
