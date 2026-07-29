package autotesting.practice_10.ui.iteration_1;

import autotesting.practice_10.models.request.CreateUserRequestDto;
import autotesting.practice_10.pages.AdminPanelPage;
import autotesting.practice_10.pages.LoginPage;
import autotesting.practice_10.pages.UserDashboardPage;
import autotesting.practice_10.supports.annotations.Browsers;
import autotesting.practice_10.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_10.testdata.AuthData.ADMIN_PASSWORD;
import static autotesting.practice_10.testdata.AuthData.ADMIN_USERNAME;

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
