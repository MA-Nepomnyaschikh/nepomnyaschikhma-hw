package autotesting.practice_3.iteration_1;

import autotesting.practice_3.generators.RandomData;
import autotesting.practice_3.models.UserRole;
import autotesting.practice_3.models.request.CreateUserRequestDto;
import autotesting.practice_3.models.request.LoginUserRequestDto;
import autotesting.practice_3.models.response.AccountResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.get.GetClientAccountsRequest;
import autotesting.practice_3.requests.post.CreateAccountRequest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.requests.post.LoginUserRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateAccountTest extends BaseTest {

    @Test
    public void authorizedUserCanCreateAccountTest() {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(RandomData.getUsername())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(createUserRequestDto);

        LoginUserRequestDto loginUserRequestDto = LoginUserRequestDto.builder()
                .username(createUserRequestDto.getUsername())
                .password(createUserRequestDto.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginUserRequestDto)
                .extract().header("Authorization");

        AccountResponseDto expectedAccount = new CreateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.created())
                .post(null)
                .extract().as(AccountResponseDto.class);

        List<AccountResponseDto> clientAccounts =  new GetClientAccountsRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract()
                .jsonPath().getList("", AccountResponseDto.class);

        softly.assertThat(clientAccounts).anySatisfy(actualAccount -> {
            softly.assertThat(actualAccount.getId()).isEqualTo(expectedAccount.getId());
            softly.assertThat(actualAccount.getAccountNumber()).isEqualTo(expectedAccount.getAccountNumber());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        });
    }

    @Test
    public void unauthorizedUserCannotCreateAccountTest() {
        new CreateAccountRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .post(null);
    }
}
