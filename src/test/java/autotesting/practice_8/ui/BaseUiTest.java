package autotesting.practice_8.ui;

import autotesting.practice_8.BaseTest;
import autotesting.practice_8.configs.Config;
import autotesting.practice_8.supports.extensions.AdminSessionExtension;
import autotesting.practice_8.supports.extensions.BrowserMatchExtension;
import autotesting.practice_8.supports.extensions.UserSessionExtension;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static com.codeborne.selenide.Selenide.executeJavaScript;

@ExtendWith(AdminSessionExtension.class)
@ExtendWith(UserSessionExtension.class)
@ExtendWith(BrowserMatchExtension.class)
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

    @AfterEach
    public void tearDown() {
        executeJavaScript("localStorage.clear();");
        executeJavaScript("sessionStorage.clear();");
        Selenide.clearBrowserCookies();
    }
}
