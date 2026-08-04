package autotesting.practice_10.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement header = $(".text-center").$("h1");
    private final SelenideElement usernameInput = $("input[placeholder='Username']");
    private final SelenideElement passwordInput = $("input[placeholder='Password']");
    private final SelenideElement loginButton = $$("button").findBy(text("Login"));


    public String url() {
        return "/login";
    }

    @Override
    public LoginPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("Login"));
        return this;
    }

    public LoginPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
        return this;
    }

}
