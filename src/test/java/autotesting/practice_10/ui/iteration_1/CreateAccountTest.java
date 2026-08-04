package autotesting.practice_10.ui.iteration_1;

import autotesting.practice_10.models.response.CreateAccountResponseDto;
import autotesting.practice_10.pages.UserDashboardPage;
import autotesting.practice_10.supports.annotations.Browsers;
import autotesting.practice_10.supports.annotations.UserSession;
import autotesting.practice_10.supports.assertions.AccountAssertions;
import autotesting.practice_10.supports.context.TestUser;
import autotesting.practice_10.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static autotesting.practice_10.testdata.expectedmessages.ui.AccountUiMessages.ACCOUNT_CREATED_SUCCESSFULLY;

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
