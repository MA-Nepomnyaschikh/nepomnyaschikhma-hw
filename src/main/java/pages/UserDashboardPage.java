package pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Getter
public class UserDashboardPage extends BasePage<UserDashboardPage> {
    public static final String WELCOME_MESSAGE = "Welcome, %s!";
    public static final String DEFAULT_WELCOME_MESSAGE = "Welcome, noname!";

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

    public UserDashboardPage shouldHaveWelcomeText(String name) {
        welcomeText.shouldBe(visible, enabled)
                .shouldHave(text(WELCOME_MESSAGE.formatted(name)));
        return this;
    }

    public UserDashboardPage shouldHaveWelcomeText() {
        welcomeText.shouldBe(visible, enabled)
                .shouldHave(text(DEFAULT_WELCOME_MESSAGE));
        return this;
    }

    public UserDashboardPage createAccount() {
        createAccountButton.shouldBe(visible, enabled).click();
        return this;
    }

    public ProfilePage openProfilePage() {
        goToProfileButton.shouldBe(visible, enabled).click();
        return new ProfilePage();
    }

    public DepositPage openDepositPage() {
        depositButton.shouldBe(visible, enabled).click();
        return new DepositPage();
    }

    public TransferPage openTransferPage() {
        transferButton.shouldBe(visible, enabled).click();
        return new TransferPage();
    }

    public String extractAccountNumber(String message) {
        if (message.isBlank()) {
            throw new IllegalArgumentException(
                    "Message cannot be blank");
        }

        Pattern pattern = Pattern.compile("ACC\\d+");
        Matcher matcher = pattern.matcher(message);

        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "Cannot extract account number from alert: " + message);
        }

        return matcher.group();
    }
}