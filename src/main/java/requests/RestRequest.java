package requests;

import configs.Config;
import models.BaseModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import supports.StepLogger;

import static io.restassured.RestAssured.given;

public class RestRequest extends HttpRequest implements CrudOperations {
    public static final String API_VERSION = Config.getProperty("apiVersion");

    public RestRequest(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return StepLogger.log("POST request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .body(model)
                    .post(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse post() {
        return StepLogger.log("POST request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .post(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse get() {
        return StepLogger.log("GET request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .get(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse getAll() {
        return StepLogger.log("GET request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .get(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse getAll(long accountId) {
        return StepLogger.log("GET request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .pathParam("accountId", accountId)
                    .get(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse put(BaseModel model) {
        return StepLogger.log("PUT request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .body(model)
                    .put(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }

    @Override
    public ValidatableResponse delete(long id) {
        return StepLogger.log("DELETE request to " + endpoint.getUrl(), () -> {

            return given()
                    .spec(requestSpecification)
                    .pathParam("id", id)
                    .delete(API_VERSION + endpoint.getUrl())
                    .then()
                    .spec(responseSpecification);

        });
    }
}
