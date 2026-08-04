package ui.iteration_2;

import models.response.CreateUserResponseDto;
import pages.ProfilePage;
import pages.UserDashboardPage;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.context.TestUser;
import testdata.UserData;
import ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_FAILED;
import static testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_SUCCESSFULLY;

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
