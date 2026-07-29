package autotesting.practice_7.ui.iteration_1;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateAccountResponseDto;
import autotesting.practice_7.pages.UserDashboardPage;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static autotesting.practice_7.validation_messages.ui.AccountUiMessages.ACCOUNT_CREATED_SUCCESSFULLY;

public class CreateAccountTest extends BaseUiTest {

    @Test
    public void userCanCreateAccountTest() {
        CreateUserRequestDto user = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(user);
        setAuthToken(token);

        UserDashboardPage userDashboard = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .createAccount();

        String alertMessage = userDashboard.getAlertMessageAndAccept();
        String accountNumber = userDashboard.extractAccountNumber(alertMessage);
        softly.assertThat(alertMessage).contains(ACCOUNT_CREATED_SUCCESSFULLY.formatted(accountNumber));

        List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(token);
        softly.assertThat(userAccounts)
                .singleElement()
                .satisfies(account -> {
                    softly.assertThat(account).isNotNull();
                    softly.assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
                    softly.assertThat(account.getBalance()).isZero();
                    softly.assertThat(account.getTransactions()).isEmpty();
                });


    }
}
