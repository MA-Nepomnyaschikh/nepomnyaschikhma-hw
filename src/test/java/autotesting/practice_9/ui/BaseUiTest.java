package autotesting.practice_9.ui;

import autotesting.practice_9.configs.Config;
import autotesting.practice_9.supports.extensions.AdminSessionExtension;
import autotesting.practice_9.supports.extensions.BrowserMatchExtension;
import autotesting.practice_9.supports.extensions.UserSessionExtension;
import autotesting.practice_9.BaseTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

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
                        "enableLog", true,
                        "enableVideo", true)
        );
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
