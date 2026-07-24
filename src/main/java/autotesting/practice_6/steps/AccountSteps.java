package autotesting.practice_6.steps;

import autotesting.practice_6.models.request.DepositRequestDto;
import autotesting.practice_6.models.request.TransferRequestDto;
import autotesting.practice_6.models.response.CreateAccountResponseDto;
import autotesting.practice_6.models.response.TransferResponseDto;
import autotesting.practice_6.requests.Endpoint;
import autotesting.practice_6.requests.RestRequest;
import autotesting.practice_6.requests.ValidatableRestRequest;
import autotesting.practice_6.specs.RequestSpecs;
import autotesting.practice_6.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static autotesting.practice_6.testdata.AccountData.MAX_DEPOSIT_AMOUNT;
import static autotesting.practice_6.testdata.AccountData.generateDepositDto;

public class AccountSteps {

    public CreateAccountResponseDto createAccount(String token) {
        return new ValidatableRestRequest<CreateAccountResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.CREATE_ACCOUNT,
                ResponseSpecs.created())
                .post();
    }

    public ValidatableResponse createAccount(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.CREATE_ACCOUNT,
                responseSpec)
                .post();
    }

    public CreateAccountResponseDto deposit(String token, DepositRequestDto dto) {
        return new ValidatableRestRequest<CreateAccountResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.DEPOSIT,
                ResponseSpecs.ok())
                .post(dto);
    }

    public String deposit(DepositRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.DEPOSIT,
                responseSpec)
                .post(dto)
                .extract().asString();
    }

    public CreateAccountResponseDto deposit(String token, int accountId, double amount) {
        DepositRequestDto dto = generateDepositDto(accountId, amount);
        return deposit(token, dto);
    }

    public CreateAccountResponseDto createAccountWithBalance(String token, double balance) {
        if (balance <= 0) {
            throw new IllegalArgumentException("Balance must be positive");
        }
        CreateAccountResponseDto account = createAccount(token);

        while (balance > 0) {
            double depositAmount = Math.min(balance, MAX_DEPOSIT_AMOUNT);

            account = deposit(token, account.getId(), depositAmount);

            balance -= depositAmount;
        }

        return account;
    }

    public List<CreateAccountResponseDto> getClientAccounts(String token) {
        return new ValidatableRestRequest<CreateAccountResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.GET_CLIENT_ACCOUNTS,
                ResponseSpecs.ok())
                .getAll();
    }

    public CreateAccountResponseDto getClientAccountById(String token, int id) {
        List<CreateAccountResponseDto> accountsList = getClientAccounts(token);

        return accountsList.stream()
                .filter(acc -> Objects.equals(acc.getId(), id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Account with id: " + id + " not found"));
    }

    public TransferResponseDto transfer(String token, TransferRequestDto dto) {
        return new ValidatableRestRequest<TransferResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.TRANSFER,
                ResponseSpecs.ok())
                .post(dto);
    }

    public String transfer(TransferRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.TRANSFER,
                responseSpec)
                .post(dto)
                .extract().asString();
    }
}
