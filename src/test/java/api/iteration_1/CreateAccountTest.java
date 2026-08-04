package api.iteration_1;

import api.BaseTest;
import models.request.CreateUserRequestDto;
import models.response.CreateAccountResponseDto;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.assertions.AccountAssertions;
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
