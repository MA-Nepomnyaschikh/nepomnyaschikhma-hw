package requests;

import models.BaseModel;
import models.request.*;
import models.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Endpoint {

    CREATE_USER(
            "/admin/users",
            CreateUserRequestDto.class,
            CreateUserResponseDto.class
    ),

    LOGIN(
            "/auth/login",
            LoginUserRequestDto.class,
            LoginUserResponseDto.class
    ),

    CREATE_ACCOUNT(
            "/accounts",
            null,
            CreateAccountResponseDto.class
    ),

    GET_CLIENT_ACCOUNTS(
            "/customer/accounts",
            null,
            CreateAccountResponseDto.class
    ),

    GET_ACCOUNT_TRANSACTIONS(
            "/accounts/{accountId}/transactions",
            null,
            TransactionResponseDto.class
    ),

    GET_ALL_USERS(
            "/admin/users",
            null,
            CreateUserResponseDto.class
    ),

    DEPOSIT(
            "/accounts/deposit",
            DepositRequestDto.class,
            CreateAccountResponseDto.class
    ),

    TRANSFER(
            "/accounts/transfer",
            TransferRequestDto.class,
            TransferResponseDto.class
    ),

    UPDATE_CUSTOMER_PROFILE(
            "/customer/profile",
            UpdateUserRequestDto.class,
            UpdateUserResponseDto.class
    ),

    GET_CUSTOMER_PROFILE(
            "/customer/profile",
            null,
            CreateUserResponseDto.class
    ),

    DELETE_USER(
            "/admin/users/{id}",
            null,
            null
    );

    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
