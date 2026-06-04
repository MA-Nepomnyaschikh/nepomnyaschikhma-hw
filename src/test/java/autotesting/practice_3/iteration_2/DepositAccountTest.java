package autotesting.practice_3.iteration_2;

import autotesting.practice_3.generators.RandomData;
import autotesting.practice_3.models.UserRole;
import autotesting.practice_3.models.request.CreateUserRequestDto;
import autotesting.practice_3.models.request.DepositRequestDto;
import autotesting.practice_3.models.request.LoginUserRequestDto;
import autotesting.practice_3.models.response.AccountResponseDto;
import autotesting.practice_3.models.response.TransactionResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.get.GetClientAccountsRequest;
import autotesting.practice_3.requests.post.CreateAccountRequest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.requests.post.DepositRequest;
import autotesting.practice_3.requests.post.LoginUserRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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
    public void authorizedUserCanDepositAccountTest(double amount) {
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

        AccountResponseDto account = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(account.getId())
                .balance(amount)
                .build();

        AccountResponseDto actualAccount = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        softly.assertThat(actualAccount.getId()).isEqualTo(account.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(account.getBalance() + amount);

        List<TransactionResponseDto> transactions = actualAccount.getTransactions();

        softly.assertThat(transactions).hasSize(1);

        TransactionResponseDto transaction = transactions.getFirst();

        softly.assertThat(transaction.getAmount()).isEqualTo(amount);
        softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(account.getId());
        softly.assertThat(transaction.getType()).isEqualTo("DEPOSIT");
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
    public void authorizedUserCannotDepositAccountWithInvalidAmountTest(double amount, String errorMessage) {

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

        AccountResponseDto account = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(account.getId())
                .balance(amount)
                .build();

        String errorResponse = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .post(depositRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        AccountResponseDto actualAccount = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", account.getId()), AccountResponseDto.class);

        softly.assertThat(actualAccount.getBalance()).isEqualTo(account.getBalance());
        softly.assertThat(actualAccount.getTransactions()).hasSize(0);
    }

    @Test
    public void authorizedUserCannotDepositIntoNonExistingAccountTest() {
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

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(-1)
                .balance(100)
                .build();

        String errorResponse = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.forbidden())
                .post(depositRequestDto)
                .extract().asString();

        Assertions.assertThat(errorResponse).isEqualTo("Unauthorized access to account");
    }

    @Test
    public void authorizedUserCannotDepositIntoAnotherUserAccountTest() {
        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(firstUser);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(firstUser.getUsername())
                .password(firstUser.getPassword())
                .build();

        String firstUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header("Authorization");

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(secondUser);

        loginRequestDto = LoginUserRequestDto.builder()
                .username(secondUser.getUsername())
                .password(secondUser.getPassword())
                .build();

        String secondUserAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header("Authorization");

        AccountResponseDto secondUserAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(secondUserAccount.getId())
                .balance(5000)
                .build();

        String errorResponse = new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.forbidden())
                .post(depositRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo("Unauthorized access to account");

        AccountResponseDto actualAccount = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", secondUserAccount.getId()), AccountResponseDto.class);

        softly.assertThat(actualAccount.getBalance()).isEqualTo(secondUserAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).hasSize(0);
    }

    @Test
    public void unauthorizedUserCannotDepositIntoAccountTest() {
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

        AccountResponseDto account = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(account.getId())
                .balance(500)
                .build();

        new DepositRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(depositRequestDto);

        AccountResponseDto actualAccount = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath()
                .getObject(String.format("find { it.id == %d }", account.getId()), AccountResponseDto.class);

        softly.assertThat(actualAccount.getBalance()).isEqualTo(account.getBalance());
        softly.assertThat(actualAccount.getTransactions()).hasSize(0);
    }

}
