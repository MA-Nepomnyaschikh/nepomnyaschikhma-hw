package ui;

import api.BaseTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import configs.Config;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import supports.AllureAttachments;
import supports.extensions.AdminSessionExtension;
import supports.extensions.BrowserMatchExtension;

import java.util.Map;

@ExtendWith(AdminSessionExtension.class)
@ExtendWith(BrowserMatchExtension.class)
public class BaseUiTest extends BaseTest {

    @BeforeAll
    public static void setUp() {
        Configuration.remote = Config.getProperty("uiRemote");
        Configuration.baseUrl = Config.getProperty("uiBaseUrl");
        Configuration.browser = Config.getProperty("browser");
        Configuration.browserSize = Config.getProperty("browserSize");
        Configuration.savePageSource = true;
        Configuration.timeout = 10000;

        Configuration.browserCapabilities.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true,
                        "enableLog", true)
        );
    }

    @BeforeEach
    public void setUpAllureListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    public void tearDown() {
        AllureAttachments.attachVideo(Selenide.sessionId().toString());
        AllureAttachments.attachLogs();
        Selenide.closeWebDriver();
    }
}
