package autotesting.practice_7.testdata;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.request.LoginUserRequestDto;

public class AuthData {

    private AuthData() {}

    public static LoginUserRequestDto generateLoginDto(CreateUserRequestDto user) {
        return LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public static LoginUserRequestDto generateLoginDto(String username, String password) {
        return LoginUserRequestDto.builder()
                .username(username)
                .password(password)
                .build();
    }
}
