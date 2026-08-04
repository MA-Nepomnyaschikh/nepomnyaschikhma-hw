package ui.iteration_1;

import models.request.CreateUserRequestDto;
import models.response.CreateUserResponseDto;
import org.junit.jupiter.api.Test;
import pages.AdminPanelPage;
import supports.annotations.AdminSession;
import supports.annotations.Browsers;
import supports.assertions.UserAssertions;
import testdata.randommodelgenerator.RandomModelGenerator;
import ui.BaseUiTest;

import java.util.List;

import static testdata.UserData.*;
import static testdata.expectedmessages.ui.UserUiMessages.*;

public class CreateUserTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @AdminSession
    public void adminCanCreateUserTest() {
        CreateUserRequestDto user = RandomModelGenerator.generate(CreateUserRequestDto.class);

        AdminPanelPage adminPanel = new AdminPanelPage()
                .open()
                .createUser(user.getUsername(), user.getPassword());

        String alertMessage = adminPanel.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(USER_CREATED_SUCCESSFULLY);

        adminPanel.getUserBadge(user).shouldBeVisible();

        softly.assertThat(adminPanel.getAllUserBadges())
                .filteredOn(userBadge ->
                        userBadge.getUsername().equals(user.getUsername()) &&
                        userBadge.getRole().equals(user.getRole()))
                .singleElement();

        CreateUserResponseDto actualUser = userSteps.getUserByUsername(user.getUsername());
        UserAssertions.assertUserCreated(softly, actualUser, user);

        cleanupManager.register(() -> userSteps.deleteUserById(actualUser.getId()));
    }

    @Test
    @Browsers(values = {"chrome"})
    @AdminSession
    public void adminCannotCreateUserWithInvalidDataTest() {
        CreateUserRequestDto user = generateUserDto(getInvalidUsername(), getPassword(), USER_ROLE);

        AdminPanelPage adminPanel = new AdminPanelPage()
                .open()
                .createUser(user.getUsername(), user.getPassword());

        String alertMessage = adminPanel.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(CREATE_USER_FAILED + CREATE_USER_INVALID_USERNAME_FORMAT);

        adminPanel.getUserBadge(user).shouldNotBeVisible();

        softly.assertThat(adminPanel.getAllUserBadges())
                .filteredOn(userBadge ->
                        userBadge.getUsername().equals(user.getUsername()) &&
                        userBadge.getRole().equals(user.getRole()))
                .isEmpty();

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(user.getUsername()))
                .isEmpty();
    }
}
