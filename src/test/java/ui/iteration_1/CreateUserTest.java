package ui.iteration_1;

import models.api.request.CreateUserRequestDto;
import models.api.response.CreateUserResponseDto;
import models.api.response.GetUserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AdminPanelPage;
import supports.StepLogger;
import supports.annotations.AdminSession;
import supports.annotations.Browsers;
import supports.assertions.UserAssertions;
import testdata.randommodelgenerator.RandomModelGenerator;
import ui.BaseUiTest;

import java.util.List;

import static testdata.UserData.*;
import static testdata.expectedmessages.ui.UserUiMessages.*;

@DisplayName("UI. Создание пользователя")
public class CreateUserTest extends BaseUiTest {

    @DisplayName("UI. Администратор может создать пользователя")
    @Test
    @Browsers(values = {"chrome"})
    @AdminSession
    public void adminCanCreateUserTest() {
        CreateUserRequestDto user = RandomModelGenerator.generate(CreateUserRequestDto.class);

        AdminPanelPage adminPanel = new AdminPanelPage();

        String alertMessage = StepLogger.uiStep("Создать пользователя ", () -> {
            return adminPanel.open()
                    .createUser(user.getUsername(), user.getPassword())
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить создание пользователя через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(USER_CREATED_SUCCESSFULLY);

            adminPanel.getUserBadge(user).shouldBeVisible();

            softly.assertThat(adminPanel.getAllUserBadges())
                    .filteredOn(userBadge ->
                            userBadge.getUsername().equals(user.getUsername()) &&
                            userBadge.getRole().equals(user.getRole()))
                    .singleElement();
        });

        StepLogger.apiStep("Проверить создание пользователя через API", () -> {
            GetUserResponseDto actualUser = userSteps.getUserByUsername(user.getUsername());
            UserAssertions.assertUserCreated(softly, actualUser, user);
            cleanupManager.register(() -> userSteps.deleteUserById(actualUser.getId()));
        });
    }

    @DisplayName("UI. Администратор не может создать пользователя с невалидными данными")
    @Test
    @Browsers(values = {"chrome"})
    @AdminSession
    public void adminCannotCreateUserWithInvalidDataTest() {
        CreateUserRequestDto user = generateUserDto(getInvalidUsername(), getPassword(), USER_ROLE);

        AdminPanelPage adminPanel = new AdminPanelPage();

        String alertMessage = StepLogger.uiStep("Создать пользователя", () -> {
            return adminPanel
                    .open()
                    .createUser(user.getUsername(), user.getPassword())
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить отсутствие пользователя через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(CREATE_USER_FAILED + CREATE_USER_INVALID_USERNAME_FORMAT);

            adminPanel.getUserBadge(user).shouldNotBeVisible();

            softly.assertThat(adminPanel.getAllUserBadges())
                    .filteredOn(userBadge ->
                            userBadge.getUsername().equals(user.getUsername()) &&
                                    userBadge.getRole().equals(user.getRole()))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить отсутствие пользователя через API", () -> {
            List<GetUserResponseDto> allUsers = userSteps.getAllUsers();
            softly.assertThat(allUsers)
                    .filteredOn(actualUser -> actualUser.getUsername().equals(user.getUsername()))
                    .isEmpty();
        });
    }
}
