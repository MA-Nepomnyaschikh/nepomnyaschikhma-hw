package autotesting.practice_8.ui.iteration_1;

import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.pages.AdminPanelPage;
import autotesting.practice_8.pages.LoginPage;
import autotesting.practice_8.pages.UserDashboardPage;
import autotesting.practice_8.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_8.testdata.AuthData.ADMIN_PASSWORD;
import static autotesting.practice_8.testdata.AuthData.ADMIN_USERNAME;

public class LoginUserTest extends BaseUiTest {

    @Test
    public void adminCanLoginWithCorrectDataTest() {

        new LoginPage()
                .open()
                .shouldBeOpened()
                .login(ADMIN_USERNAME, ADMIN_PASSWORD)
                .getPage(AdminPanelPage.class)
                .shouldBeOpened();
    }

    @Test
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
