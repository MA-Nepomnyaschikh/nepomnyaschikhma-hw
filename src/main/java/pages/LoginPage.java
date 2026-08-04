package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
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

    public LoginPage setUsername(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
        usernameInput.shouldHave(value(username));
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        passwordInput.shouldHave(value(password));
        return this;
    }

    public LoginPage login() {
        loginButton.shouldBe(enabled).click();
        return this;
    }

    public LoginPage login(String username, String password) {
        setUsername(username);
        setPassword(password);
        login();
        return this;
    }

}
