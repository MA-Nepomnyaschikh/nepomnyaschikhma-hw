package autotesting.practice_6.tests.ui.iteration_1;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.response.CreateUserResponseDto;
import autotesting.practice_6.testdata.UserData;
import autotesting.practice_6.tests.BaseTest;
import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class CreateUserTest extends BaseTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.baseUrl = "http://192.168.3.2:3000";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";

        Configuration.browserCapabilities.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true,
                        "enableLog", true)
        );
    }

    @Test
    public void adminCanCreateUserTest() {
        CreateUserRequestDto admin = CreateUserRequestDto.builder().username("admin").password("admin").build();

        Selenide.open("/login");

        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $x("//button[text()='Login']").click();

        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);

        CreateUserRequestDto user = UserData.generateRandomUserDto();

        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(user.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(user.getPassword());
        $x("//button[text()='Add User']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).isEqualTo("✅ User created successfully!");
        alert.accept();

        ElementsCollection allUsersFromUi = $(Selectors.byText("All Users")).parent().findAll("li");
        allUsersFromUi.findBy(Condition.exactText(user.getUsername() + "\nUSER")).shouldBe(Condition.visible);

        CreateUserResponseDto actualUser = userSteps.getUserByUsername(user.getUsername());
        softly.assertThat(actualUser)
                .usingRecursiveComparison()
                .comparingOnlyFields("username", "role")
                .isEqualTo(user);
    }

    @Test
    public void adminCannotCreateUserWithInvalidDataTest() {
        CreateUserRequestDto admin = CreateUserRequestDto.builder().username("admin").password("admin").build();

        Selenide.open("/login");

        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $x("//button[text()='Login']").click();

        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);

        CreateUserRequestDto user = UserData.generateUserDto("a", UserData.getPassword(), UserData.USER_ROLE);

        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(user.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(user.getPassword());
        $x("//button[text()='Add User']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Failed to create user:");
        softly.assertThat(alert.getText()).contains("username: Username must be between 3 and 15 characters");
        alert.accept();

        ElementsCollection allUsersFromUi = $(Selectors.byText("All Users")).parent().findAll("li");
        allUsersFromUi.findBy(Condition.exactText(user.getUsername() + "\nUSER")).shouldNotBe(Condition.exist);

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(user.getUsername()))
                .isEmpty();
    }
}
