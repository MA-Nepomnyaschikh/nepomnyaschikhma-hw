package api.steps;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import api.models.request.CreateUserRequestDto;
import api.models.request.LoginUserRequestDto;
import api.requests.Endpoint;
import api.requests.RestRequest;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import static api.specs.ResponseSpecs.AUTH_HEADER;
import static common.testdata.factories.AuthData.generateLoginDto;

public class AuthSteps {

    public ValidatableResponse login(LoginUserRequestDto loginDto) {
        return new RestRequest(
                RequestSpecs.unauth(),
                Endpoint.LOGIN,
                ResponseSpecs.ok())
                .post(loginDto);
    }

    public ValidatableResponse login(LoginUserRequestDto loginDto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.LOGIN,
                responseSpec)
                .post(loginDto);
    }

    public String loginAndGetToken(CreateUserRequestDto userDto) {
        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        return login(loginDto)
                .extract()
                .header(AUTH_HEADER);
    }
}
