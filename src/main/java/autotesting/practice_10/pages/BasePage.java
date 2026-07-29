package autotesting.practice_10.pages;

import autotesting.practice_10.pages.elements.BaseElement;
import autotesting.practice_10.supports.utils.WaitUtils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;

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
        Alert alert = WaitUtils.waitUntil(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public static void setAuthToken(String authToken) {
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", authToken);
    }

    protected <T extends BaseElement> List<T> mapToElementsList(ElementsCollection elements, Function<SelenideElement, T> constructor) {
        return elements
                .stream()
                .map(constructor)
                .toList();
    }

    protected void shouldHaveOptionWithText(SelenideElement select, String text) {
        select.$$("option")
                .findBy(text(text))
                .shouldBe(exist)
                .shouldBe(enabled);
    }
}
