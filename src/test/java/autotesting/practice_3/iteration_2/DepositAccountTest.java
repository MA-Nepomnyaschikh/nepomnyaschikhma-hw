package autotesting.practice_3.iteration_2;

import autotesting.practice_3.generators.TestData;
import autotesting.practice_3.contract.enams.TransactionType;
import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.request.CreateUserRequestDto;
import autotesting.practice_3.contract.models.request.DepositRequestDto;
import autotesting.practice_3.contract.models.request.LoginUserRequestDto;
import autotesting.practice_3.contract.models.response.AccountResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.get.GetClientAccountsRequest;
import autotesting.practice_3.requests.post.CreateAccountRequest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.requests.post.DepositRequest;
import autotesting.practice_3.requests.post.LoginUserRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static autotesting.practice_3.generators.TestData.NON_EXISTING_ACCOUNT_ID;
import static autotesting.practice_3.generators.TestData.getRandomValidDepositAmount;
import static autotesting.practice_3.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_3.utils.AccountUtils.findById;

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
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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

        AccountResponseDto expectedAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(expectedAccount.getId())
                .balance(depositAmount)
                .build();

        AccountResponseDto accountResponseDto = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .post(depositRequestDto)
                .extract()
                .as(AccountResponseDto.class);

        softly.assertThat(accountResponseDto.getId()).isEqualTo(expectedAccount.getId());
        softly.assertThat(accountResponseDto.getBalance()).isEqualTo(expectedAccount.getBalance() + depositAmount);

        softly.assertThat(accountResponseDto.getTransactions()).anySatisfy(transaction -> {
            softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
            softly.assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
            softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(expectedAccount.getId());
        });

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualAccount = findById(accountsList, expectedAccount.getId());

        softly.assertThat(actualAccount.getId()).isEqualTo(expectedAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance() + depositAmount);

        softly.assertThat(actualAccount.getTransactions()).anySatisfy(transaction -> {
            softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
            softly.assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
            softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(expectedAccount.getId());
        });
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

        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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

        AccountResponseDto expectedAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(expectedAccount.getId())
                .balance(depositAmount)
                .build();

        String errorResponse = new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .post(depositRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(errorMessage);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualAccount = findById(accountsList, expectedAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    public void authorizedUserCannotDepositIntoNonExistingAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(NON_EXISTING_ACCOUNT_ID)
                .balance(depositAmount)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.forbidden())
                .post(depositRequestDto);
    }

    @Test
    public void authorizedUserCannotDepositIntoAnotherUserAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto firstUser = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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
                .extract().header(AUTH_HEADER);

        CreateUserRequestDto secondUser = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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
                .extract().header(AUTH_HEADER);

        AccountResponseDto expectedSecondUserAcc = new CreateAccountRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(expectedSecondUserAcc.getId())
                .balance(depositAmount)
                .build();

        new DepositRequest(
                RequestSpecs.authAsUser(firstUserAuthHeader),
                ResponseSpecs.forbidden())
                .post(depositRequestDto);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(secondUserAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualSecondUserAcc = findById(accountsList, expectedSecondUserAcc.getId());

        softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(expectedSecondUserAcc.getBalance());
        softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();
    }

    @Test
    public void unauthorizedUserCannotDepositIntoAccountTest() {
        double depositAmount = getRandomValidDepositAmount();

        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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

        AccountResponseDto expectedAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post()
                .extract().as(AccountResponseDto.class);

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .id(expectedAccount.getId())
                .balance(depositAmount)
                .build();

        new DepositRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(depositRequestDto);

        List<AccountResponseDto> accountsList = new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .as(new TypeRef<List<AccountResponseDto>>() {});

        AccountResponseDto actualAccount = findById(accountsList, expectedAccount.getId());

        softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

}
