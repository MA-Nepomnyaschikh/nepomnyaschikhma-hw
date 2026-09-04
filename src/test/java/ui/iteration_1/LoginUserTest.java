package ui.iteration_1;

import api.models.request.CreateUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.AdminPanelPage;
import ui.pages.LoginPage;
import ui.pages.UserDashboardPage;
import common.allure.StepLogger;
import common.annotations.Browsers;
import ui.BaseUiTest;

import static common.testdata.factories.AuthData.ADMIN_PASSWORD;
import static common.testdata.factories.AuthData.ADMIN_USERNAME;

@DisplayName("UI. Авторизация пользователя")
public class LoginUserTest extends BaseUiTest {

    @DisplayName("UI. Администратор может авторизоваться с валидными данными")
    @Test
    @Browsers(values = {"chrome"})
    public void adminCanLoginWithCorrectDataTest() {

        StepLogger.uiStep("Авторизоваться под пользователем", () -> {
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
        CreateUserRequestDto user = StepLogger.apiStep("Создать пользователя", () -> {
            return userSteps.createRandomUser();
        });

        StepLogger.uiStep("Авторизоваться под пользователем", () -> {
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
