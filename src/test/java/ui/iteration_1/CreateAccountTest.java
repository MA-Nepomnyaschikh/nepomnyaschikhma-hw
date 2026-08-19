package ui.iteration_1;

import models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.Test;
import pages.UserDashboardPage;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;
import ui.BaseUiTest;

import java.util.List;

import static testdata.expectedmessages.ui.AccountUiMessages.ACCOUNT_CREATED_SUCCESSFULLY;

public class CreateAccountTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCanCreateAccountTest(TestUser user) {
        UserDashboardPage userDashboard = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .createAccount();

        String alertMessage = userDashboard.getAlertMessageAndAccept();
        String accountNumber = userDashboard.extractAccountNumber(alertMessage);
        softly.assertThat(alertMessage).isEqualTo(ACCOUNT_CREATED_SUCCESSFULLY.formatted(accountNumber));

        List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(user.getToken());
        softly.assertThat(userAccounts)
                .singleElement()
                .satisfies(account -> {
                    AccountAssertions.assertAccountCreated(softly, account);
                    softly.assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
                });
    }
}
