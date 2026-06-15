package autotesting.practice_3.requests.get;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class GetAccountTransactionsRequest extends GetRequest {
    private final int accountId;

    public GetAccountTransactionsRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification, int accountId) {
        super(requestSpecification, responseSpecification);
        this.accountId = accountId;
    }

    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .pathParam("accountId", accountId)
                .get("api/v1/accounts/{accountId}/transactions")
                .then()
                .spec(responseSpecification);
    }
}
