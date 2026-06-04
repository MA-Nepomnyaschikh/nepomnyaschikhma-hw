package autotesting.practice_3.iteration_2;

import autotesting.practice_3.generators.RandomData;
import autotesting.practice_3.models.UserRole;
import autotesting.practice_3.models.request.CreateUserRequestDto;
import autotesting.practice_3.models.request.DepositRequestDto;
import autotesting.practice_3.models.request.LoginUserRequestDto;
import autotesting.practice_3.models.request.TransferRequestDto;
import autotesting.practice_3.models.response.AccountResponseDto;
import autotesting.practice_3.models.response.TransactionResponseDto;
import autotesting.practice_3.models.response.TransferResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.get.GetAccountTransactionsRequest;
import autotesting.practice_3.requests.get.GetClientAccountsRequest;
import autotesting.practice_3.requests.post.*;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

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
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstAccount.getId())
                .balance(5000)
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
                .post(null)
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstAccount.getId())
                .receiverAccountId(secondAccount.getId())
                .amount(transferAmount)
                .build();

        TransferResponseDto transferResponseDto = new TransferRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(transferRequestDto)
                .extract().as(TransferResponseDto.class);

        softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(expectedFirstAcc.getId());
        softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(secondAccount.getId());
        softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferAmount);
        softly.assertThat(transferResponseDto.getMessage()).isEqualTo("Transfer successful");

        AccountResponseDto actualSecondAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondAcc.getBalance()).isEqualTo(transferAmount);
        softly.assertThat(actualSecondAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_IN");
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedFirstAcc.getId());
        });

        AccountResponseDto actualFirstAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", expectedFirstAcc.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstAcc.getBalance()).isEqualTo(expectedFirstAcc.getBalance() - transferAmount, within(0.00001));
        softly.assertThat(actualFirstAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(secondAccount.getId());
        });
    }

    @MethodSource("validAmountProvider")
    @ParameterizedTest
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double transferAmount) {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(5000)
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
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
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
        softly.assertThat(transferResponseDto.getMessage()).isEqualTo("Transfer successful");

        AccountResponseDto actualSecondUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(transferAmount);
        softly.assertThat(actualSecondUserAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_IN");
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(expectedFirstUserAcc.getId());
        });

        AccountResponseDto actualFirstUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", firstUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance() - transferAmount, within(0.00001));
        softly.assertThat(actualFirstUserAcc.getTransactions()).anySatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
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
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstAccount.getId())
                .balance(5000)
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
                .post(null)
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

        AccountResponseDto actualSecondAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondAcc.getBalance()).isEqualTo(secondAccount.getBalance());
        softly.assertThat(actualSecondAcc.getTransactions()).hasSize(0);

        AccountResponseDto actualFirstAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", firstAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstAcc.getBalance()).isEqualTo(expectedFirstAcc.getBalance());
        softly.assertThat(actualFirstAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
        });
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(double transferAmount, String errorMessage) {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(5000)
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
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(secondUserAccount.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        AccountResponseDto actualSecondUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).hasSize(0);

        AccountResponseDto actualFirstUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", expectedFirstUserAcc.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
        });
    }

    @Test
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest() {
        double transferAmount = 5000.01;

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(5000)
                .build();

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(secondUserAccount.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo("Invalid transfer: insufficient funds or invalid accounts");

        AccountResponseDto actualSecondUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).hasSize(0);

        AccountResponseDto actualFirstUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", expectedFirstUserAcc.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
            softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(secondUserAccount.getId());
        });
    }


    @Test
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest() {
        double transferAmount = 100;

        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto userAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(userAccount.getId())
                .balance(5000)
                .build();

        AccountResponseDto expectedUserAccount = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(userAccount.getId())
                .receiverAccountId(-1)
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo("Invalid transfer: insufficient funds or invalid accounts");

        AccountResponseDto actualUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", expectedUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualUserAcc.getBalance()).isEqualTo(expectedUserAccount.getBalance());
        softly.assertThat(actualUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
        });
    }

    @Test
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest() {
        double transferAmount = 100;

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(5000)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(-1)
                .receiverAccountId(secondUserAccount.getId())
                .amount(transferAmount)
                .build();

        String errorResponse = new TransferRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.forbidden())
                .post(transferRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo("Unauthorized access to account");

        AccountResponseDto actualSecondUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).hasSize(0);
    }

    @Test
    public void unauthorizedUserCannotTransferFundsTest() {
        double transferAmount = 100;

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto firstUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(firstUserAccount.getId())
                .balance(5000)
                .build();

        AccountResponseDto expectedFirstUserAcc = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
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
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .senderAccountId(firstUserAccount.getId())
                .receiverAccountId(secondUserAccount.getId())
                .amount(transferAmount)
                .build();

        new TransferRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(transferRequestDto);

        AccountResponseDto actualSecondUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).hasSize(0);

        AccountResponseDto actualFirstUserAcc = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", expectedFirstUserAcc.getId()), AccountResponseDto.class);

        softly.assertThat(actualFirstUserAcc.getBalance()).isEqualTo(expectedFirstUserAcc.getBalance());
        softly.assertThat(actualFirstUserAcc.getTransactions()).noneSatisfy(actualTransaction -> {
            softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferAmount);
            softly.assertThat(actualTransaction.getType()).isEqualTo("TRANSFER_OUT");
        });
    }

}
