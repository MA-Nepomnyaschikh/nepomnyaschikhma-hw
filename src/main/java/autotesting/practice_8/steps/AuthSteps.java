package autotesting.practice_8.steps;

import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.request.LoginUserRequestDto;
import autotesting.practice_8.requests.Endpoint;
import autotesting.practice_8.requests.RestRequest;
import autotesting.practice_8.specs.RequestSpecs;
import autotesting.practice_8.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;

import static autotesting.practice_8.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_8.testdata.AuthData.generateLoginDto;

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
