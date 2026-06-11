package autotesting.practice_3.requests.put;

import autotesting.practice_3.contract.models.BaseModel;
import autotesting.practice_3.requests.Request;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class PutRequest extends Request {
    public PutRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    public abstract ValidatableResponse put(BaseModel model);
}
