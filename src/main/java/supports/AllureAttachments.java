package supports;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import configs.Config;
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

    public static void attachVideo(String sessionId) {
        String url =
                Config.getProperty("uiRemote")
                .replace("/wd/hub", "")
                + "/video/"
                + sessionId
                + ".mp4";

        Allure.addAttachment(
                "Video HTML",
                "text/html",
                "<html><body>" +
                        "<video controls autoplay width='100%'>" +
                        "<source src='" + url + "' type='video/mp4'>" +
                        "</video>" +
                        "</body></html>"
        );
    }
}
