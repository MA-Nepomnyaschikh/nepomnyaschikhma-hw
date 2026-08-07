package ui.iteration_1;

import models.request.CreateUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AdminPanelPage;
import pages.LoginPage;
import pages.UserDashboardPage;
import supports.StepLogger;
import supports.annotations.Browsers;
import ui.BaseUiTest;

import static testdata.AuthData.ADMIN_PASSWORD;
import static testdata.AuthData.ADMIN_USERNAME;

@DisplayName("UI. Авторизация пользователя")
public class LoginUserTest extends BaseUiTest {

    @DisplayName("UI. Администратор может авторизоваться с валидными данными")
    @Test
    @Browsers(values = {"chrome"})
    public void adminCanLoginWithCorrectDataTest() {

        StepLogger.log("Авторизоваться  под пользователем", () -> {
        new LoginPage()
                .open()
                .shouldBeOpened()
                .login(ADMIN_USERNAME, ADMIN_PASSWORD)
                .getPage(AdminPanelPage.class)
                .shouldBeOpened();
        });
    }

    @DisplayName("UI. Пользователь может авторизоваться с валидными данными")
    @Test
    @Browsers(values = {"chrome"})
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestDto user = StepLogger.log("Создать пользователя", () -> {
            return userSteps.createRandomUser();
        });

        StepLogger.log("Авторизоваться под пользователем", () -> {
            new LoginPage()
                    .open()
                    .shouldBeOpened()
                    .login(user.getUsername(), user.getPassword())
                    .getPage(UserDashboardPage.class)
                    .shouldBeOpened()
                    .shouldHaveWelcomeText();
        });
    }
}
