package ui.iteration_1;

import models.request.CreateUserRequestDto;
import pages.AdminPanelPage;
import pages.LoginPage;
import pages.UserDashboardPage;
import supports.annotations.Browsers;
import ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static testdata.AuthData.ADMIN_PASSWORD;
import static testdata.AuthData.ADMIN_USERNAME;

public class LoginUserTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    public void adminCanLoginWithCorrectDataTest() {

        new LoginPage()
                .open()
                .shouldBeOpened()
                .login(ADMIN_USERNAME, ADMIN_PASSWORD)
                .getPage(AdminPanelPage.class)
                .shouldBeOpened();
    }

    @Test
    @Browsers(values = {"chrome"})
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestDto user = userSteps.createRandomUser();

        new LoginPage()
                .open()
                .shouldBeOpened()
                .login(user.getUsername(), user.getPassword())
                .getPage(UserDashboardPage.class)
                .shouldBeOpened()
                .shouldHaveWelcomeText();
    }

}
