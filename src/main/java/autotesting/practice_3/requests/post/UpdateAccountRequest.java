package autotesting.practice_3.requests.post;

import autotesting.practice_3.models.BaseModel;
import autotesting.practice_3.requests.put.PutRequest;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class UpdateAccountRequest extends PutRequest {
    public UpdateAccountRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse put(BaseModel model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .when()
                .put("/api/v1/customer/profile")
                .then()
                .spec(responseSpecification);
    }
}
