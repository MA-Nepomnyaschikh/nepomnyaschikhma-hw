package autotesting.practice_7.ui.iteration_1;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateUserResponseDto;
import autotesting.practice_7.pages.AdminPanelPage;
import autotesting.practice_7.supports.comparisons.UserComparisons;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static autotesting.practice_7.testdata.UserData.*;
import static autotesting.practice_7.validation_messages.ui.UserUiMessages.*;

public class CreateUserTest extends BaseUiTest {

    @Test
    public void adminCanCreateUserTest() {
        CreateUserRequestDto admin = generateAdminDto();
        setAuthToken(admin);
        CreateUserRequestDto user = generateRandomUserDto();

        AdminPanelPage adminPanel = new AdminPanelPage()
                .open()
                .createUser(user.getUsername(), user.getPassword());

        String alertMessage = adminPanel.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).isEqualTo(USER_CREATED_SUCCESSFULLY);

        adminPanel.shouldHaveUserInUsersList(user);

        CreateUserResponseDto actualUser = userSteps.getUserByUsername(user.getUsername());
        softly.assertThat(actualUser)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisons.CREATE_USER.fields())
                .isEqualTo(user);

        cleanupManager.register(() -> userSteps.deleteUserById(actualUser.getId()));
    }

    @Test
    public void adminCannotCreateUserWithInvalidDataTest() {
        CreateUserRequestDto admin = generateAdminDto();
        setAuthToken(admin);
        CreateUserRequestDto user = generateUserDto(getName(), getPassword(), USER_ROLE);

        AdminPanelPage adminPanel = new AdminPanelPage()
                .open()
                .createUser(user.getUsername(), user.getPassword());

        String alertMessage = adminPanel.getAlertMessageAndAccept();
        softly.assertThat(alertMessage).contains(CREATE_USER_FAILED_HEADER);
        softly.assertThat(alertMessage).contains(CREATE_USER_INVALID_USERNAME);

        adminPanel.shouldNotHaveUserInUsersList(user);

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(user.getUsername()))
                .isEmpty();
    }
}
