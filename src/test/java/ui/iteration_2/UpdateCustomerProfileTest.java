package ui.iteration_2;

import models.response.CreateUserResponseDto;
import org.junit.jupiter.api.DisplayName;
import pages.ProfilePage;
import pages.UserDashboardPage;
import supports.StepLogger;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.context.TestUser;
import testdata.UserData;
import ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_FAILED;
import static testdata.expectedmessages.ui.UserUiMessages.UPDATE_USER_SUCCESSFULLY;

public class UpdateCustomerProfileTest extends BaseUiTest {

    @DisplayName("UI. Пользователь может изменить имя в профиле")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCanSetValidNameInProfileTest(TestUser user) {
        String newName = UserData.getValidName();

        String alertMessage = StepLogger.log("Изменить имя пользователя", () -> {
            return new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openProfilePage()
                .shouldBeOpened()
                .changeUserName(newName)
                .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить изменение имени пользователя через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_SUCCESSFULLY);

            new ProfilePage()
                    .logout()
                    .shouldBeOpened()
                    .login(user.getUsername(), user.getPassword())
                    .getPage(UserDashboardPage.class)
                    .shouldBeOpened()
                    .shouldHaveWelcomeText(newName);
        });

        StepLogger.log("Проверить изменение имени пользователя через API", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isEqualTo(newName);
        });
    }

    @DisplayName("UI. Пользователь не может изменить имя в профиле на невалидное")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCannotSetInvalidNameInProfileTest(TestUser user) {
        String newName = UserData.getUsername();

        String alertMessage = StepLogger.log("Изменить имя пользователя", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openProfilePage()
                    .shouldBeOpened()
                    .changeUserName(newName)
                    .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить отсутствие изменений пользователя через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(UPDATE_USER_FAILED);

            new ProfilePage()
                    .logout()
                    .shouldBeOpened()
                    .login(user.getUsername(), user.getPassword())
                    .getPage(UserDashboardPage.class)
                    .shouldBeOpened()
                    .shouldHaveWelcomeText();
        });

        StepLogger.log("Проверить отсутствие изменений пользователя через API", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isNull();
        });
    }
}
