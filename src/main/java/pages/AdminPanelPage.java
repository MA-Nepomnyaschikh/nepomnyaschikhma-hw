package pages;

import models.request.CreateUserRequestDto;
import pages.elements.UserBadge;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import supports.StepLogger;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Getter
public class AdminPanelPage extends BasePage<AdminPanelPage> {

    private final SelenideElement header = $(".container-center").$("h1");
    private final SelenideElement usernameInput = $("input[placeholder='Username']");
    private final SelenideElement passwordInput = $("input[placeholder='Password']");
    private final SelenideElement createUserButton = $$("button").findBy(text("Add User"));
    private final ElementsCollection allUsers = $(Selectors.byText("All Users")).parent().findAll("li");

    @Override
    public String url() {
        return "/admin";
    }

    @Override
    public AdminPanelPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("Admin Panel"));
        return this;
    }

    public AdminPanelPage setUsername(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
        usernameInput.shouldHave(value(username));
        return this;
    }

    public AdminPanelPage setPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        passwordInput.shouldHave(value(password));
        return this;
    }

    public AdminPanelPage createUser() {
        createUserButton.shouldBe(enabled).click();
        return this;
    }

    public AdminPanelPage createUser(String username, String password) {
        setUsername(username);
        setPassword(password);
        createUser();
        return this;
    }

    public List<UserBadge> getAllUserBadges() {
        return StepLogger.log("Get all users from Admin Panel", () -> {
            return mapToElementsList(getAllUsers(), UserBadge::new);
        });
    }

    public UserBadge getUserBadge(CreateUserRequestDto userDto) {
        return StepLogger.log("Get user from Admin Panel", () -> {
            SelenideElement root = allUsers.findBy(ownText(userDto.getUsername()));
            return new UserBadge(root);
        });
    }
}
