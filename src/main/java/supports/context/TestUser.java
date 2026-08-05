package supports.context;

import lombok.Getter;
import models.request.CreateUserRequestDto;
import models.response.CreateUserResponseDto;

@Getter
public class TestUser {

    private final CreateUserRequestDto requestDto;
    private final CreateUserResponseDto responseDto;
    private final long id;
    private final String username;
    private final String password;
    private final String role;
    private final String token;

    public TestUser(CreateUserRequestDto requestDto, CreateUserResponseDto responseDto, String token) {
        this.requestDto = requestDto;
        this.responseDto = responseDto;
        this.id = responseDto.getId();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.role = requestDto.getRole();
        this.token = token;
    }

}
