package autotesting.practice_8.api.iteration_1;

import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.response.CreateAccountResponseDto;
import autotesting.practice_8.specs.RequestSpecs;
import autotesting.practice_8.specs.ResponseSpecs;
import autotesting.practice_8.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CreateAccountTest extends BaseTest {

    @Test
    public void authorizedUserCanCreateAccountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        CreateAccountResponseDto createdAccount = accountSteps.createAccount(userAuthHeader);

        softly.assertThat(createdAccount.getId()).isNotNull().isPositive();
        softly.assertThat(createdAccount.getBalance()).isZero();
        softly.assertThat(createdAccount.getAccountNumber()).isNotNull();
        softly.assertThat(createdAccount.getTransactions()).isEmpty();

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, createdAccount.getId());

        softly.assertThat(actualAccount)
                .usingRecursiveComparison()
                .isEqualTo(createdAccount);
    }

    @Test
    public void unauthorizedUserCannotCreateAccountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        accountSteps.createAccount(RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(userAuthHeader);

        softly.assertThat(userAccounts).isEmpty();
    }
}
