package autotesting.practice_6.tests.ui.iteration_1;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.response.CreateAccountResponseDto;
import autotesting.practice_6.tests.BaseTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class CreateAccountTest extends BaseTest {

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
    public void userCanCreateAccountTest() {
        CreateUserRequestDto user = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(user);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");

        Selenide.open("/dashboard");

        $(Selectors.byText("➕ Create New Account")).click();

        Alert alert = switchTo().alert();
        String accountNumber = alert.getText().split(":")[1].trim();
        softly.assertThat(alert.getText()).contains("✅ New Account Created! Account Number: " + accountNumber);
        alert.accept();

        List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(token);
        softly.assertThat(userAccounts)
                .singleElement()
                .satisfies(account -> {
                    softly.assertThat(account).isNotNull();
                    softly.assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
                    softly.assertThat(account.getBalance()).isZero();
                });


    }
}
