package autotesting.practice_9.api.iteration_1;

import autotesting.practice_9.models.request.CreateUserRequestDto;
import autotesting.practice_9.models.response.CreateAccountResponseDto;
import autotesting.practice_9.specs.RequestSpecs;
import autotesting.practice_9.specs.ResponseSpecs;
import autotesting.practice_9.supports.assertions.AccountAssertions;
import autotesting.practice_9.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CreateAccountTest extends BaseTest {

    @Test
    public void authorizedUserCanCreateAccountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        CreateAccountResponseDto createdAccount = accountSteps.createAccount(userAuthHeader);
        AccountAssertions.assertAccountCreated(softly, createdAccount);

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
