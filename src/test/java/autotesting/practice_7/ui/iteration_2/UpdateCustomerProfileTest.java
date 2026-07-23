package autotesting.practice_7.ui.iteration_2;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateUserResponseDto;
import autotesting.practice_7.pages.ProfilePage;
import autotesting.practice_7.pages.UserDashboardPage;
import autotesting.practice_7.testdata.UserData;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_7.validation_messages.ui.UserUiMessages.UPDATE_USER_FAILED;
import static autotesting.practice_7.validation_messages.ui.UserUiMessages.UPDATE_USER_SUCCESSFULLY;

public class UpdateCustomerProfileTest extends BaseUiTest {

    @Test
    public void userCanSetValidNameInProfileTest() {
        CreateUserRequestDto user = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(user);

        String newName = UserData.getName();

        setAuthToken(token);

        ProfilePage profilePage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openProfilePage()
                .shouldBeOpened()
                .setNewName(newName)
                .saveChanges();

        String alertMessage = profilePage.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_SUCCESSFULLY);

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(token);
        softly.assertThat(actualUser.getName()).isEqualTo(newName);

        profilePage.logout()
                .shouldBeOpened()
                .loginAs(user)
                .getPage(UserDashboardPage.class)
                .shouldBeOpened()
                .shouldHaveWelcomeText(newName);
    }

    @Test
    public void userCannotSetInvalidNameInProfileTest() {
        CreateUserRequestDto user = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(user);

        String newName = UserData.getUsername();

        setAuthToken(token);

        ProfilePage profilePage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openProfilePage()
                .shouldBeOpened()
                .setNewName(newName)
                .saveChanges();

        String alertMessage = profilePage.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_FAILED);

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(token);
        softly.assertThat(actualUser.getName()).isNull();

        profilePage.logout()
                .shouldBeOpened()
                .loginAs(user)
                .getPage(UserDashboardPage.class)
                .shouldBeOpened()
                .shouldHaveWelcomeText();
    }
}
