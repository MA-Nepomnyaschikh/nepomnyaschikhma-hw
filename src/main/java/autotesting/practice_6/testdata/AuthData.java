package autotesting.practice_6.testdata;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.request.LoginUserRequestDto;

public class AuthData {

    public static final String ADMIN_LOGIN = "admin";
    public static final String ADMIN_PASSWORD = "admin";

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
