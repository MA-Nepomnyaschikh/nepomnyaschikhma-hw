package autotesting.practice_4.tests.iteration_2;

import autotesting.practice_4.tests.BaseTest;
import autotesting.practice_4.models.request.CreateUserRequestDto;
import autotesting.practice_4.models.request.DepositRequestDto;
import autotesting.practice_4.models.response.CreateAccountResponseDto;
import autotesting.practice_4.specs.RequestSpecs;
import autotesting.practice_4.specs.ResponseSpecs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_4.messages.AccountMessages.DEPOSIT_UNAUTHORIZED;
import static autotesting.practice_4.testdata.AccountData.*;

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
        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = userSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);
        CreateAccountResponseDto depositResponse = userSteps.deposit(userAuthHeader, depositRequestDto);

        softly.assertThat(depositResponse.getId()).isEqualTo(userAccount.getId());
        softly.assertThat(depositResponse.getBalance()).isEqualTo(userAccount.getBalance() + depositAmount);

        softly.assertThat(depositResponse.getTransactions())
                .singleElement()
                .satisfies(transaction -> {
                    softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
                    softly.assertThat(transaction.getType()).isEqualTo(DEPOSIT);
                    softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(userAccount.getId());
        });

        CreateAccountResponseDto actualAccount = userSteps.getClientAccountById(userAuthHeader, userAccount.getId());

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
        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto expectedAccount = userSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(expectedAccount.getId(), depositAmount);
        String errorResponse = userSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        CreateAccountResponseDto actualAccount = userSteps.getClientAccountById(userAuthHeader, expectedAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    public void authorizedUserCannotDepositNonExistingAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        DepositRequestDto depositRequestDto = generateDepositDto(NON_EXISTING_ACCOUNT_ID, depositAmount);
        String errorResponse = userSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.forbidden());

        softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
    }

    @Test
    public void authorizedUserCannotDepositAnotherUserAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto firstUserDto = adminSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);

        CreateUserRequestDto secondUser = adminSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUser);
        CreateAccountResponseDto expectedSecondUserAcc = userSteps.createAccount(secondUserAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(expectedSecondUserAcc.getId(), depositAmount);
        String errorResponse = userSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(firstUserAuthHeader), ResponseSpecs.forbidden());

        softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);

        CreateAccountResponseDto actualSecondUserAcc = userSteps.getClientAccountById(secondUserAuthHeader, expectedSecondUserAcc.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAcc.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();
    }

    @Test
    public void unauthorizedUserCannotDepositIntoAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto userDto = adminSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto expectedAccount = userSteps.createAccount(userAuthHeader);

        DepositRequestDto depositRequestDto = generateDepositDto(expectedAccount.getId(), depositAmount);
        userSteps.deposit(depositRequestDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        CreateAccountResponseDto actualAccount = userSteps.getClientAccountById(userAuthHeader, expectedAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

}
