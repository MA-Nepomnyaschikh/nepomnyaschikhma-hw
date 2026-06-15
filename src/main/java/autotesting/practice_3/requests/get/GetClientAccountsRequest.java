package autotesting.practice_3.requests.get;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class GetClientAccountsRequest extends GetRequest {

    public GetClientAccountsRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .get("api/v1/customer/accounts")
                .then()
                .spec(responseSpecification);
    }

}
