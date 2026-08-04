package autotesting.practice_10.ui.iteration_1;

import autotesting.practice_10.models.request.CreateUserRequestDto;
import autotesting.practice_10.models.response.CreateUserResponseDto;
import autotesting.practice_10.pages.AdminPanelPage;
import autotesting.practice_10.supports.annotations.AdminSession;
import autotesting.practice_10.supports.annotations.Browsers;
import autotesting.practice_10.supports.assertions.UserAssertions;
import autotesting.practice_10.testdata.randommodelgenerator.RandomModelGenerator;
import autotesting.practice_10.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static autotesting.practice_10.testdata.UserData.*;
import static autotesting.practice_10.testdata.expectedmessages.ui.UserUiMessages.*;

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
