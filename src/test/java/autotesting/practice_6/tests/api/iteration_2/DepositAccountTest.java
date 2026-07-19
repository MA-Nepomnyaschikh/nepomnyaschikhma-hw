package autotesting.practice_6.tests.api.iteration_2;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.request.DepositRequestDto;
import autotesting.practice_6.models.response.CreateAccountResponseDto;
import autotesting.practice_6.specs.RequestSpecs;
import autotesting.practice_6.specs.ResponseSpecs;
import autotesting.practice_6.tests.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_6.messages.AccountMessages.DEPOSIT_UNAUTHORIZED;
import static autotesting.practice_6.testdata.AccountData.*;

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
        CreateAccountResponseDto userAccount = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);
        CreateAccountResponseDto depositResponse = accountSteps.deposit(userAuthHeader, depositRequestDto);

        softly.assertThat(depositResponse.getId()).isEqualTo(userAccount.getId());
        softly.assertThat(depositResponse.getBalance()).isEqualTo(userAccount.getBalance() + depositAmount);

        softly.assertThat(depositResponse.getTransactions())
                .singleElement()
                .satisfies(transaction -> {
                    softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
                    softly.assertThat(transaction.getType()).isEqualTo(DEPOSIT);
                    softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(userAccount.getId());
        });

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, userAccount.getId());

        softly.assertThat(actualAccount)
                .usingRecursiveComparison()
                .isEqualTo(depositResponse);
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
        CreateAccountResponseDto userAccount = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);
        String errorResponse = accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, userAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
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
        CreateAccountResponseDto userAccount = accountSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);
        accountSteps.deposit(depositRequestDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(userAuthHeader, userAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

}
