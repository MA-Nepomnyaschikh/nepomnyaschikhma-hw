package steps;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.api.request.DepositRequestDto;
import models.api.request.TransferRequestDto;
import models.api.response.*;
import requests.Endpoint;
import requests.RestRequest;
import requests.ValidatableRestRequest;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.math.BigDecimal;
import java.util.Comparator;
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

    public DepositResponseDto deposit(String token, DepositRequestDto dto) {
        return new ValidatableRestRequest<DepositResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.DEPOSIT,
                ResponseSpecs.ok())
                .post(dto);
    }

    public ValidatableResponse deposit(DepositRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.DEPOSIT,
                responseSpec)
                .post(dto);
    }

    public DepositResponseDto deposit(String token, long accountId, BigDecimal amount) {
        DepositRequestDto dto = generateDepositDto(accountId, amount);
        return deposit(token, dto);
    }

    public CreateAccountResponseDto createAccountWithBalance(String token, BigDecimal balance) {
        if (balance.signum() <= 0) {
            throw new IllegalArgumentException("Balance must be positive");
        }

        CreateAccountResponseDto account = createAccount(token);
        BigDecimal remainingBalance = balance;

        while (remainingBalance.signum() > 0) {
            BigDecimal depositAmount = remainingBalance.min(MAX_DEPOSIT_AMOUNT);

            deposit(token, account.getId(), depositAmount);

            remainingBalance = remainingBalance.subtract(depositAmount);
        }

        return getClientAccountById(token, account.getId());
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

    public TransferWithFraudCheckResponseDto transferWithFraudCheck(String token, TransferRequestDto dto) {
        return new ValidatableRestRequest<TransferWithFraudCheckResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.TRANSFER_WITH_FRAUD_CHECK,
                ResponseSpecs.ok())
                .post(dto);
    }

    public ValidatableResponse transfer(TransferRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.TRANSFER,
                responseSpec)
                .post(dto);
    }

    public ValidatableResponse transferWithFraudCheck(TransferRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.TRANSFER_WITH_FRAUD_CHECK,
                responseSpec)
                .post(dto);
    }

    public List<TransactionResponseDto> getAccountTransactions(String token, long accountId) {
        return new ValidatableRestRequest<TransactionResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.GET_ACCOUNT_TRANSACTIONS,
                ResponseSpecs.ok())
                .getAll(accountId)
                .stream()
                .sorted(Comparator.comparing(TransactionResponseDto::getId))
                .toList();
    }

    public ValidatableResponse getAccountTransactions(long accountId, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.GET_ACCOUNT_TRANSACTIONS,
                responseSpec)
                .getAll(accountId);
    }

    public TransactionResponseDto getAccountTransactionById(String token, long accountId, long transactionId) {
        return getAccountTransactions(token, accountId)
                .stream()
                .filter(transaction -> transaction.getId() == transactionId)
                .findFirst()
                .orElseThrow();
    }
}
