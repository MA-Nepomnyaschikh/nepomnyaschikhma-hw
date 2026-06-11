package autotesting.practice_3.requests.post;

import autotesting.practice_3.contract.models.BaseModel;
import autotesting.practice_3.requests.Request;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class PostRequest extends Request {

    public PostRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        super(requestSpecification, responseSpecification);
    }

    public abstract ValidatableResponse post(BaseModel model);
}
