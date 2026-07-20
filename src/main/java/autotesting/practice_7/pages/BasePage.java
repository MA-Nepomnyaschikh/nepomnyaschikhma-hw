package autotesting.practice_7.pages;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Selenide.switchTo;

public abstract class BasePage<T extends BasePage> {

    public abstract String url();

    public T open() {
        return Selenide.open(url(), (Class<T>) this.getClass());
    }

    public abstract T shouldBeOpened();

    public <T extends BasePage> T getPage(Class<T> pageClass) {
        return Selenide.page(pageClass);
    }

    public String getAlertMessageAndAccept() {
        Alert alert = switchTo().alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }
}
