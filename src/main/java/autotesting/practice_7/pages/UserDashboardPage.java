package autotesting.practice_7.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Getter
public class UserDashboardPage extends BasePage<UserDashboardPage> {
    private final SelenideElement header = $(".text-center").$("h1");
    private final SelenideElement welcomeText = $(Selectors.byClassName("welcome-text"));
    private final SelenideElement goToProfileButton = $(".profile-header");
    private final SelenideElement depositButton = $$("button").findBy(text("Deposit Money"));
    private final SelenideElement transferButton = $$("button").findBy(text("Make a Transfer"));
    private final SelenideElement createAccountButton = $(Selectors.byText("➕ Create New Account"));

    @Override
    public String url() {
        return "/dashboard";
    }

    @Override
    public UserDashboardPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("User Dashboard"));
        return this;
    }

    public UserDashboardPage shouldHaveWelcomeText(String text) {
        welcomeText.shouldHave(text(text));
        return this;
    }

    public UserDashboardPage createAccount() {
        createAccountButton.click();
        return this;
    }

    public ProfilePage openProfilePage() {
        goToProfileButton.click();
        return new ProfilePage();
    }

    public DepositPage openDepositPage() {
        depositButton.click();
        return new DepositPage();
    }

    public TransferPage openTransferPage() {
        transferButton.click();
        return new TransferPage();
    }
}
