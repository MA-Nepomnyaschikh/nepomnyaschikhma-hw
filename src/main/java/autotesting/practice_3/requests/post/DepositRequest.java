package autotesting.practice_3.requests.post;

import autotesting.practice_3.contract.models.BaseModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class DepositRequest extends PostRequest {
    public DepositRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .when()
                .post("api/v1/accounts/deposit")
                .then()
                .spec(responseSpecification);
    }
}
