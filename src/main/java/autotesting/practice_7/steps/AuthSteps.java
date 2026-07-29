package autotesting.practice_7.steps;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.request.LoginUserRequestDto;
import autotesting.practice_7.requests.Endpoint;
import autotesting.practice_7.requests.RestRequest;
import autotesting.practice_7.specs.RequestSpecs;
import autotesting.practice_7.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;

import static autotesting.practice_7.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_7.testdata.AuthData.generateLoginDto;

public class AuthSteps {

    public ValidatableResponse login(LoginUserRequestDto loginDto) {

        return new RestRequest(
                RequestSpecs.unauth(),
                Endpoint.LOGIN,
                ResponseSpecs.ok())
                .post(loginDto);
    }

    public String loginAndGetToken(CreateUserRequestDto userDto) {
        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        return login(loginDto)
                .extract()
                .header(AUTH_HEADER);
    }
}
