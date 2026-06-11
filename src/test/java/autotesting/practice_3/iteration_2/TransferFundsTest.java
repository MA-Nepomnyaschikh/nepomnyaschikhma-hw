package autotesting.practice_3.iteration_2;

import autotesting.practice_3.BaseTest;
import autotesting.practice_3.contract.enams.TransactionType;
import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.request.CreateUserRequestDto;
import autotesting.practice_3.contract.models.request.DepositRequestDto;
import autotesting.practice_3.contract.models.request.LoginUserRequestDto;
import autotesting.practice_3.contract.models.request.TransferRequestDto;
import autotesting.practice_3.contract.models.response.AccountResponseDto;
import autotesting.practice_3.contract.models.response.TransferResponseDto;
import autotesting.practice_3.requests.get.GetClientAccountsRequest;
import autotesting.practice_3.requests.post.*;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static autotesting.practice_3.contract.messages.DepositMessages.UNAUTHORIZED_DEPOSIT;
import static autotesting.practice_3.contract.messages.TransferMessages.*;
import static autotesting.practice_3.generators.TestData.*;
import static autotesting.practice_3.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_3.utils.AccountUtils.findById;
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
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto);

        AccountResponseDto expectedFirstAcc = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        AccountResponseDto expectedSecondAcc = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstAccount.getId())
                .receiverAccountId(expectedSecondAcc.getId())
                .amount(transferAmount)
                .build();

        TransferResponseDto transferResponseDto = new TransferRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(transferRequestDto)
                .extract().as(TransferResponseDto.class);

        softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(expectedFirstAcc.getId());
        softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(expectedSecondAcc.getId());
        softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferAmount);
        softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_SUCCESSFUL);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondAcc = findById(accountsList, expectedSecondAcc.getId());

        softly.assertThat(actualSecondAcc.getBalance()).isEqualTo(transferAmount);
        softly.assertThat(actualSecondAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_IN.toString());
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedFirstAcc.getId());
        });

        AccountResponseDto actualFirstAcc = findById(accountsList, expectedFirstAcc.getId());

        softly.assertThat(actualFirstAcc.getBalance()).isEqualTo(expectedFirstAcc.getBalance() - transferAmount, within(0.00001));
        softly.assertThat(actualFirstAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedSecondAcc.getId());
        });
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double transferAmount) {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto firstUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(firstUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto);

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        LoginUserRequestDto secondUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(secondUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(secondUserAccount.getId())
                .amount(transferAmount)
                .build();

        TransferResponseDto transferResponseDto = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(transferRequestDto)
                .extract().as(TransferResponseDto.class);

        softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(expectedFirstUserAcc.getId());
        softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(secondUserAccount.getId());
        softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferAmount);
        softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_SUCCESSFUL);

        List<AccountResponseDto> secondUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(secondUserAccountsList, secondUserAccount.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(transferAmount);
        softly.assertThat(actualSecondUserAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_IN.toString());
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedFirstUserAcc.getId());
        });

        List<AccountResponseDto> firstUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualFirstUserAcc = findById(firstUserAccountsList, firstUserAccount.getId());

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance() - transferAmount, within(0.00001));
        softly.assertThat(actualFirstUserAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(secondUserAccount.getId());
        });
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
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto);

        AccountResponseDto expectedFirstAcc = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        AccountResponseDto secondAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstAccount.getId())
                .receiverAccountId(secondAccount.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondAcc = findById(accountsList, secondAccount.getId());

        softly.assertThat(actualSecondAcc.getBalance()).isEqualTo(secondAccount.getBalance());
        softly.assertThat(actualSecondAcc.getTransactions()).isEmpty();

        AccountResponseDto actualFirstAcc = findById(accountsList, firstAccount.getId());

        softly.assertThat(actualFirstAcc.getBalance()).isEqualTo(expectedFirstAcc.getBalance());
        softly.assertThat(actualFirstAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
        });
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(double transferAmount, String errorMessage) {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto firstUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(firstUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto);

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        LoginUserRequestDto secondUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(secondUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto expectedSecondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(expectedSecondUserAccount.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        List<AccountResponseDto> secondUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(secondUserAccountsList, expectedSecondUserAccount.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();

        List<AccountResponseDto> firstUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualFirstUserAcc = findById(firstUserAccountsList, expectedFirstUserAcc.getId());

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
        });
    }

    @Test
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest() {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto firstUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(firstUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        LoginUserRequestDto secondUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(secondUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto expectedSecondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        double transferAmountExceedingBalance = expectedFirstUserAcc.getBalance() + 0.01;

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(expectedSecondUserAccount.getId())
                .amount(transferAmountExceedingBalance)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);

        List<AccountResponseDto> secondUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(secondUserAccountsList, expectedSecondUserAccount.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();

        List<AccountResponseDto> firstUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualFirstUserAcc = findById(firstUserAccountsList, expectedFirstUserAcc.getId());

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmountExceedingBalance);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedSecondUserAccount.getId());
        });
    }


    @Test
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto userLoginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(userLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto userAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(userAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        AccountResponseDto expectedUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(userAccount.getId())
                .receiverAccountId(NON_EXISTING_ACCOUNT_ID)
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualUserAcc = findById(accountsList, expectedUserAcc.getId());

        softly.assertThat(actualUserAcc.getBalance()).isEqualTo(expectedUserAcc.getBalance());
        softly.assertThat(actualUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
        });
    }

    @Test
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto firstUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(firstUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        LoginUserRequestDto secondUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(secondUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto expectedSecondUserAcc = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(NON_EXISTING_ACCOUNT_ID)
                .receiverAccountId(expectedSecondUserAcc.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.forbidden())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(UNAUTHORIZED_DEPOSIT);

        List<AccountResponseDto> secondUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(secondUserAccountsList, expectedSecondUserAcc.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAcc.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();
    }

    @Test
    public void unauthorizedUserCannotTransferFundsTest() {
        double transferAmount = getRandomValidTransferAmount();

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto firstUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(firstUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(MAX_DEPOSIT_AMOUNT)
                .build();

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(getUsername())
                .password(getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        LoginUserRequestDto secondUserLoginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(secondUserLoginRequestDto)
                .extract().header(AUTH_HEADER);

        AccountResponseDto expectedSecondUserAcc = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(expectedSecondUserAcc.getId())
                .amount(transferAmount)
                .build();

        new TransferRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(transferRequestDto);

        List<AccountResponseDto> secondUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(secondUserAccountsList, expectedSecondUserAcc.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAcc.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();

        List<AccountResponseDto> firstUserAccountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualFirstUserAcc = findById(firstUserAccountsList, expectedFirstUserAcc.getId());

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo(TransactionType.TRANSFER_OUT.toString());
        });
    }



}
