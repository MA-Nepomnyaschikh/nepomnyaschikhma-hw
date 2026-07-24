package autotesting.practice_8.api.iteration_2;

import autotesting.practice_8.BaseTest;
import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.request.DepositRequestDto;
import autotesting.practice_8.models.response.CreateAccountResponseDto;
import autotesting.practice_8.specs.RequestSpecs;
import autotesting.practice_8.specs.ResponseSpecs;
import autotesting.practice_8.supports.assertions.AccountAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_8.testdata.AccountData.*;
import static autotesting.practice_8.testdata.expectedmessages.api.AccountApiMessages.DEPOSIT_UNAUTHORIZED;

public class DepositAccountTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(5000.00),
                Arguments.of(4999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanDepositAccountTest(double depositAmount) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto accountBeforeDeposit = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(accountBeforeDeposit.getId(), depositAmount);
        CreateAccountResponseDto accountAfterDeposit = accountSteps.deposit(userAuthHeader, depositRequestDto);

        AccountAssertions.assertDepositCompleted(softly, accountAfterDeposit, accountBeforeDeposit, depositAmount);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, accountBeforeDeposit.getId());
        softly.assertThat(actualAccount)
                .usingRecursiveComparison()
                .isEqualTo(accountAfterDeposit);
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(5000.01, "Deposit amount cannot exceed 5000"),
                Arguments.of(0, "Deposit amount must be at least 0.01"),
                Arguments.of(-0.01, "Deposit amount must be at least 0.01")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotDepositAccountWithInvalidAmountTest(double depositAmount, String errorMessage) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto accountBeforeDeposit = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(accountBeforeDeposit.getId(), depositAmount);
        String errorResponse = accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, accountBeforeDeposit.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(accountBeforeDeposit.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    public void authorizedUserCannotDepositNonExistingAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        DepositRequestDto depositRequestDto = generateDepositDto(NON_EXISTING_ACCOUNT_ID, depositAmount);
        String errorResponse = accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.forbidden());

        softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
    }

    @Test
    public void authorizedUserCannotDepositAnotherUserAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);

        CreateUserRequestDto secondUser = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUser);
        CreateAccountResponseDto secondUserAccount = accountSteps.createAccount(secondUserAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(secondUserAccount.getId(), depositAmount);
        String errorResponse = accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(firstUserAuthHeader), ResponseSpecs.forbidden());

        softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);

        CreateAccountResponseDto actualSecondUserAcc = accountSteps.getClientAccountById(secondUserAuthHeader, secondUserAccount.getId());
        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();
    }

    @Test
    public void unauthorizedUserCannotDepositIntoAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto actualReceiverUserAcc = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(actualReceiverUserAcc.getId(), depositAmount);
        accountSteps.deposit(depositRequestDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, actualReceiverUserAcc.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(actualReceiverUserAcc.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

}
