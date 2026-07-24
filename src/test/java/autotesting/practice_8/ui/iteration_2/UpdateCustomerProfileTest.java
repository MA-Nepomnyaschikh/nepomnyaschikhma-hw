package autotesting.practice_8.ui.iteration_2;

import autotesting.practice_8.models.response.CreateUserResponseDto;
import autotesting.practice_8.pages.ProfilePage;
import autotesting.practice_8.pages.UserDashboardPage;
import autotesting.practice_8.supports.annotations.Browsers;
import autotesting.practice_8.supports.annotations.UserSession;
import autotesting.practice_8.supports.context.TestUser;
import autotesting.practice_8.testdata.UserData;
import autotesting.practice_8.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_8.testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_FAILED;
import static autotesting.practice_8.testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_SUCCESSFULLY;

public class UpdateCustomerProfileTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCanSetValidNameInProfileTest(TestUser user) {
        String newName = UserData.getName();

        ProfilePage profilePage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openProfilePage()
                .shouldBeOpened()
                .setNewName(newName)
                .saveChanges();

        String alertMessage = profilePage.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_SUCCESSFULLY);

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
        softly.assertThat(actualUser.getName()).isEqualTo(newName);

        profilePage.logout()
                .shouldBeOpened()
                .login(user.getUsername(), user.getPassword())
                .getPage(UserDashboardPage.class)
                .shouldBeOpened()
                .shouldHaveWelcomeText(newName);
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCannotSetInvalidNameInProfileTest(TestUser user) {
        String newName = UserData.getUsername();

        ProfilePage profilePage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openProfilePage()
                .shouldBeOpened()
                .setNewName(newName)
                .saveChanges();

        String alertMessage = profilePage.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_FAILED);

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
        softly.assertThat(actualUser.getName()).isNull();

        profilePage.logout()
                .shouldBeOpened()
                .login(user.getUsername(), user.getPassword())
                .getPage(UserDashboardPage.class)
                .shouldBeOpened()
                .shouldHaveWelcomeText();
    }
}
