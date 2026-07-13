package autotesting.practice_4.tests.iteration_1;

import autotesting.practice_4.tests.BaseTest;
import autotesting.practice_4.models.request.CreateUserRequestDto;
import autotesting.practice_4.models.response.CreateAccountResponseDto;
import autotesting.practice_4.specs.RequestSpecs;
import autotesting.practice_4.specs.ResponseSpecs;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CreateAccountTest extends BaseTest {

    @Test
    public void authorizedUserCanCreateAccountTest() {
        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        CreateAccountResponseDto createdAccount = userSteps.createAccount(userAuthHeader);

        softly.assertThat(createdAccount.getId()).isPositive();
        softly.assertThat(createdAccount.getBalance()).isZero();
        softly.assertThat(createdAccount.getAccountNumber()).isNotNull();
        softly.assertThat(createdAccount.getTransactions()).isEmpty();

        CreateAccountResponseDto actualAccount = userSteps.getClientAccountById(userAuthHeader, createdAccount.getId());

        softly.assertThat(actualAccount)
                .usingRecursiveComparison()
                .isEqualTo(createdAccount);
    }

    @Test
    public void unauthorizedUserCannotCreateAccountTest() {
        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        userSteps.createAccount(RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        List<CreateAccountResponseDto> userAccounts = userSteps.getClientAccounts(userAuthHeader);

        softly.assertThat(userAccounts).isEmpty();
    }
}
