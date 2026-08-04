package autotesting.practice_9.ui.iteration_2;

import autotesting.practice_9.models.response.CreateUserResponseDto;
import autotesting.practice_9.pages.ProfilePage;
import autotesting.practice_9.pages.UserDashboardPage;
import autotesting.practice_9.supports.annotations.Browsers;
import autotesting.practice_9.supports.annotations.UserSession;
import autotesting.practice_9.supports.context.TestUser;
import autotesting.practice_9.testdata.UserData;
import autotesting.practice_9.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_9.testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_FAILED;
import static autotesting.practice_9.testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_SUCCESSFULLY;

public class UpdateCustomerProfileTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCanSetValidNameInProfileTest(TestUser user) {
        String newName = UserData.getValidName();

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
