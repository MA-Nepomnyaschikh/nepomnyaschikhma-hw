package autotesting.practice_9.requests;

import autotesting.practice_9.models.BaseModel;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ValidatableRestRequest<T extends BaseModel> extends HttpRequest implements CrudOperations {
    private RestRequest restRequest;

    public ValidatableRestRequest(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.restRequest = new RestRequest(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public T post(BaseModel model) {
        return (T) restRequest.post(model).extract().as(endpoint.getResponseModel());
    }

    @Override
    public T post() {
        return (T) restRequest.post().extract().as(endpoint.getResponseModel());
    }

    @Override
    public T get() {
        return (T) restRequest.get().extract().as(endpoint.getResponseModel());
    }

    @Override
    public List<T> getAll() {
        Class<?> arrayClass = Array.newInstance(endpoint.getResponseModel(), 0).getClass();
        T[] array = (T[]) restRequest.getAll().extract().as(arrayClass);
        return Arrays.asList(array);
    }

    @Override
    public T put(BaseModel model) {
        return (T) restRequest.put(model).extract().as(endpoint.getResponseModel());
    }

    @Override
    public Object delete(long id) {
        return null;
    }
}
