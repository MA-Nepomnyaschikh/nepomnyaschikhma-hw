package autotesting.practice_9.supports.extensions;

import autotesting.practice_9.models.request.CreateUserRequestDto;
import autotesting.practice_9.models.request.LoginUserRequestDto;
import autotesting.practice_9.models.response.CreateUserResponseDto;
import autotesting.practice_9.requests.Endpoint;
import autotesting.practice_9.requests.RestRequest;
import autotesting.practice_9.requests.ValidatableRestRequest;
import autotesting.practice_9.specs.RequestSpecs;
import autotesting.practice_9.specs.ResponseSpecs;
import autotesting.practice_9.supports.annotations.UserSession;
import autotesting.practice_9.supports.context.TestUser;
import autotesting.practice_9.testdata.UserData;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static autotesting.practice_9.pages.BasePage.setAuthToken;
import static autotesting.practice_9.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_9.testdata.AuthData.generateLoginDto;

public class UserSessionExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace USER_SESSIONS = ExtensionContext.Namespace.create(UserSessionExtension.class);
    private static final String USERS_KEY = "users";

    @Override
    public void beforeEach(ExtensionContext context) {
        // Шаг 1: Проверить, есть ли у теста аннотация UserSession
        UserSession annotation = context.getRequiredTestMethod().getAnnotation(UserSession.class);
        // Шаг 2: Если аннотации нет - выйти
        if (annotation == null) {
            return;
        }
        // Шаг 3: Если аннотация есть - создать нужное количество пользователей
        List<TestUser> users = createUsers(annotation);
        // Шаг 4: Положить созданных пользователей в контекст теста
        saveUsers(context, users);
        // Шаг 5: Положить токен нужного пользователя в локал сторейдж
        loginInBrowser(annotation, users);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestMethod().isAnnotationPresent(UserSession.class)
                && parameterContext.getParameter().getType().equals(TestUser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        List<TestUser> users = (List<TestUser>) context.getStore(USER_SESSIONS).get(USERS_KEY);

        int userIndex = getTestUserIndex(parameterContext);

        if (userIndex >= users.size()) {
            throw new IllegalStateException(
                    String.format(
                            "Expected at least %d test users, but only %d were created.",
                            userIndex + 1,
                            users.size()));
        }

        return users.get(userIndex);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        List<TestUser> users = (List<TestUser>) context.getStore(USER_SESSIONS).remove(USERS_KEY);

        if (users == null) {
            return;
        }

        for (TestUser user : users) {
            new RestRequest(
                    RequestSpecs.authAsAdmin(),
                    Endpoint.DELETE_USER,
                    ResponseSpecs.ok())
                    .delete(user.getId());
        }
    }

    private List<TestUser> createUsers(UserSession annotation) {
        List<TestUser> users = new ArrayList<>();

        for (int i = 0; i < annotation.usersCount(); i++) {
            users.add(createUser());
        }

        return users;
    }

    private TestUser createUser() {
        CreateUserRequestDto requestDto = UserData.generateRandomUserDto();
        CreateUserResponseDto responseDto = new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsAdmin(),
                Endpoint.CREATE_USER,
                ResponseSpecs.created())
                .post(requestDto);

        LoginUserRequestDto loginDto = generateLoginDto(requestDto);
        String token = new RestRequest(
                RequestSpecs.unauth(),
                Endpoint.LOGIN,
                ResponseSpecs.ok())
                .post(loginDto)
                .extract()
                .header(AUTH_HEADER);

        return new TestUser(requestDto, responseDto, token);
    }

    private void loginInBrowser(UserSession annotation, List<TestUser> users) {
        int userIndex = annotation.authUserNumber() -1;
        setAuthToken(users.get(userIndex).getToken());
    }

    private void saveUsers(ExtensionContext context, List<TestUser> users) {
        context.getStore(USER_SESSIONS).put(USERS_KEY, users);
    }

    private int getTestUserIndex(ParameterContext context) {

        Parameter[] parameters =
                context.getDeclaringExecutable().getParameters();

        int currentIndex = context.getIndex();

        int userIndex = 0;

        for (int i = 0; i < currentIndex; i++) {
            if (parameters[i].getType().equals(TestUser.class)) {
                userIndex++;
            }
        }

        return userIndex;
    }
}
