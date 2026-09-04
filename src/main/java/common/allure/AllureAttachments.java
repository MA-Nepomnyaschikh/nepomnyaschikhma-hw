package common.allure;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogType;

import java.io.ByteArrayInputStream;

public class AllureAttachments {

    public static void attachScreenshot() {
        Allure.addAttachment(
                "Page screen",
                "image/png",
                new ByteArrayInputStream(Selenide.screenshot(OutputType.BYTES)),
                ".png"
        );
    }

    public static void attachSource() {
        Allure.addAttachment(
                "Page source",
                "text/html",
                WebDriverRunner.getWebDriver().getPageSource()
        );
    }

    public static void attachLogs() {
        String logs = String.join(
                "\n",
                Selenide.getWebDriverLogs(LogType.BROWSER)
        );

        Allure.addAttachment(
                "Browser logs",
                "text/plain",
                logs
        );
    }
}
