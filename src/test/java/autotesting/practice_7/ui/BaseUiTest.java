package autotesting.practice_7.ui;

import autotesting.practice_7.BaseTest;
import autotesting.practice_7.configs.Config;
import autotesting.practice_7.models.request.CreateUserRequestDto;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BaseUiTest extends BaseTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = Config.getProperty("uiRemote");
        Configuration.baseUrl = Config.getProperty("uiBaseUrl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("browserSize");

        Configuration.browserCapabilities.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true,
                        "enableLog", true)
        );
    }

    public void setAuthToken(String authToken) {
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", authToken);
    }

    public void setAuthToken(CreateUserRequestDto userDto) {
        String token = authSteps.loginAndGetToken(userDto);
        setAuthToken(token);
    }

    @AfterEach
    public void tearDown() {
        executeJavaScript("localStorage.clear();");
        executeJavaScript("sessionStorage.clear();");
        Selenide.clearBrowserCookies();
    }
}
