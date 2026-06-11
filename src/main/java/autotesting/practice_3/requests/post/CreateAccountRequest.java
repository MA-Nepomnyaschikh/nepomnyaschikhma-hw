package autotesting.practice_3.requests.post;

import autotesting.practice_3.contract.models.BaseModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class CreateAccountRequest extends PostRequest {
    public CreateAccountRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given()
                .spec(requestSpecification)
                .post("api/v1/accounts")
                .then()
                .spec(responseSpecification);
    }

    public ValidatableResponse post() {
        return given()
                .spec(requestSpecification)
                .post("api/v1/accounts")
                .then()
                .spec(responseSpecification);
    }
}
