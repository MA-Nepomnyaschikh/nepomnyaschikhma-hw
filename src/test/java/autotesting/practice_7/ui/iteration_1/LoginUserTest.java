package autotesting.practice_7.ui.iteration_1;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.pages.AdminPanelPage;
import autotesting.practice_7.pages.LoginPage;
import autotesting.practice_7.pages.UserDashboardPage;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_7.testdata.UserData.generateAdminDto;

public class LoginUserTest extends BaseUiTest {

    @Test
    public void adminCanLoginWithCorrectDataTest() {
        CreateUserRequestDto admin = generateAdminDto();

        new LoginPage()
                .open()
                .shouldBeOpened()
                .login(admin.getUsername(), admin.getPassword())
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
                .shouldHaveWelcomeText("Welcome, noname!");
    }

}
