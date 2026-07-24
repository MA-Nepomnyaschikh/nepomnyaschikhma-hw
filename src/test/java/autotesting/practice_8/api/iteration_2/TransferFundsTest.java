package autotesting.practice_8.api.iteration_2;

import autotesting.practice_8.BaseTest;
import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.request.TransferRequestDto;
import autotesting.practice_8.models.response.CreateAccountResponseDto;
import autotesting.practice_8.models.response.TransferResponseDto;
import autotesting.practice_8.specs.RequestSpecs;
import autotesting.practice_8.specs.ResponseSpecs;
import autotesting.practice_8.supports.assertions.AccountAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_8.testdata.AccountData.*;
import static autotesting.practice_8.testdata.expectedmessages.api.AccountApiMessages.*;
import static org.assertj.core.api.Assertions.within;

public class TransferFundsTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(10000.00),
                Arguments.of(9999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountBetweenTheirAccountsTest(double transferAmount) {

        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(userAuthHeader, MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(userAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
        TransferResponseDto transferResponseDto = accountSteps.transfer(userAuthHeader, transferDto);

        AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(userAuthHeader, receiverAccount.getId());
        AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(userAuthHeader, senderAccount.getId());
        AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double transferAmount) {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserAuthHeader, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
        TransferResponseDto transferResponseDto = accountSteps.transfer(firstUserAuthHeader, transferDto);

        AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(secondUserAuthHeader, receiverAccount.getId());
        AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(firstUserAuthHeader, senderAccount.getId());
        AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(10000.01, "Transfer amount cannot exceed 10000"),
                Arguments.of(0, "Transfer amount must be at least 0.01"),
                Arguments.of(-0.01, "Transfer amount must be at least 0.01")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountBetweenTheirAccountsTest(double transferAmount, String errorMessage) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(userAuthHeader, MAX_TRANSFER_AMOUNT);

        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(userAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
        String errorResponse = accountSteps.transfer(transferDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(userAuthHeader, receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(userAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(double transferAmount, String errorMessage) {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserAuthHeader, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
        String errorResponse = accountSteps.transfer(transferDto, RequestSpecs.authAsUser(firstUserAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(secondUserAuthHeader, receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(firstUserAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }

    @Test
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest() {
        double transferAmountExceedingBalance = MAX_DEPOSIT_AMOUNT + 0.01;

        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserAuthHeader, MAX_DEPOSIT_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmountExceedingBalance);
        String errorResponse = accountSteps.transfer(transferDto, RequestSpecs.authAsUser(firstUserAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(secondUserAuthHeader, receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(firstUserAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }


    @Test
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(userAuthHeader, MAX_TRANSFER_AMOUNT);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), NON_EXISTING_ACCOUNT_ID, transferAmount);
        String errorResponse = accountSteps.transfer(transferDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest());

        softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(userAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }

    @Test
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserAuthHeader, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(NON_EXISTING_ACCOUNT_ID, receiverAccount.getId(), transferAmount);
        String errorResponse = accountSteps.transfer(transferDto, RequestSpecs.authAsUser(firstUserAuthHeader), ResponseSpecs.forbidden());

        softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(secondUserAuthHeader, receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(firstUserAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }

    @Test
    public void unauthorizedUserCannotTransferFundsTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserAuthHeader = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserAuthHeader, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserAuthHeader = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserAuthHeader);

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
        accountSteps.transfer(transferDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(secondUserAuthHeader, receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();

        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(firstUserAuthHeader, senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
    }
}