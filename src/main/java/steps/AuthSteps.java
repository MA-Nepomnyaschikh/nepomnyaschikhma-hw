package steps;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.request.CreateUserRequestDto;
import models.request.LoginUserRequestDto;
import requests.Endpoint;
import requests.RestRequest;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import static specs.ResponseSpecs.AUTH_HEADER;
import static testdata.AuthData.generateLoginDto;

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
