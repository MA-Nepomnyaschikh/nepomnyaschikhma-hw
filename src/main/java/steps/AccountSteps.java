package steps;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.api.request.DepositRequestDto;
import models.api.request.TransferRequestDto;
import models.api.response.CreateAccountResponseDto;
import models.api.response.TransactionResponseDto;
import models.api.response.TransferResponseDto;
import requests.Endpoint;
import requests.RestRequest;
import requests.ValidatableRestRequest;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static testdata.AccountData.MAX_DEPOSIT_AMOUNT;
import static testdata.AccountData.generateDepositDto;

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

    public CreateAccountResponseDto deposit(String token, long accountId, BigDecimal amount) {
        DepositRequestDto dto = generateDepositDto(accountId, amount);
        return deposit(token, dto);
    }

    public CreateAccountResponseDto createAccountWithBalance(String token, BigDecimal balance) {
        double remainingBalance = balance.doubleValue();

        if (remainingBalance <= 0) {
            throw new IllegalArgumentException("Balance must be positive");
        }

        CreateAccountResponseDto account = createAccount(token);

        while (remainingBalance > 0) {
            double depositAmount = Math.min(remainingBalance, MAX_DEPOSIT_AMOUNT.doubleValue());

            account = deposit(token, account.getId(), BigDecimal.valueOf(depositAmount));

            remainingBalance -= depositAmount;
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

    public ValidatableResponse getClientAccounts(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.GET_CLIENT_ACCOUNTS,
                responseSpec)
                .getAll();
    }

    public CreateAccountResponseDto getClientAccountById(String token, long id) {
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

    public List<TransactionResponseDto> getAccountTransactions(String token, long accountId) {
        return new ValidatableRestRequest<TransactionResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.GET_ACCOUNT_TRANSACTIONS,
                ResponseSpecs.ok())
                .getAll(accountId);
    }

    public ValidatableResponse getAccountTransactions(long accountId, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.GET_ACCOUNT_TRANSACTIONS,
                responseSpec)
                .getAll(accountId);
    }
}
