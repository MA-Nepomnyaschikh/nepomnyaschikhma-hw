package autotesting.practice_10.testdata;

import autotesting.practice_10.configs.Config;
import autotesting.practice_10.models.request.CreateUserRequestDto;
import autotesting.practice_10.models.request.LoginUserRequestDto;

public class AuthData {

    public static final String ADMIN_USERNAME = Config.getProperty("admin.username");
    public static final String ADMIN_PASSWORD = Config.getProperty("admin.password");
    public static final String ADMIN_TOKEN = Config.getProperty("admin.token");

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
