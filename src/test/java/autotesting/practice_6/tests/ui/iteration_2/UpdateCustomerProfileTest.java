package autotesting.practice_6.tests.ui.iteration_2;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.response.CreateUserResponseDto;
import autotesting.practice_6.testdata.UserData;
import autotesting.practice_6.tests.BaseTest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class UpdateCustomerProfileTest extends BaseTest {

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
    public void userCanSetValidNameInProfileTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);

        String newName = UserData.getName();

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/edit-profile");

        $("div.text-center").$("h1").shouldHave(Condition.text("✏\uFE0F Edit Profile")).shouldBe(Condition.visible);

        $("input[placeholder='Enter new name']").setValue(newName);

        $x("//button[text()='\uD83D\uDCBE Save Changes']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("✅ Name updated successfully!");
        alert.accept();

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(token);
        softly.assertThat(actualUser.getName()).isEqualTo(newName);
    }

    @Test
    public void userCannotSetInvalidNameInProfileTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);

        String newName = UserData.getUsername();

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/edit-profile");

        $("div.text-center").$("h1").shouldHave(Condition.text("✏\uFE0F Edit Profile")).shouldBe(Condition.visible);

        $("input[placeholder='Enter new name']").setValue(newName);

        $x("//button[text()='\uD83D\uDCBE Save Changes']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("Name must contain two words with letters only");
        alert.accept();

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(token);
        softly.assertThat(actualUser.getName()).isNull();
    }
}
