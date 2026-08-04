package autotesting.practice_10.pages;

import autotesting.practice_10.models.request.CreateUserRequestDto;
import autotesting.practice_10.pages.elements.UserBadge;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

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

    public AdminPanelPage createUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        createUserButton.click();
        return this;
    }

    public List<UserBadge> getAllUserBadges() {
        return mapToElementsList(getAllUsers(), UserBadge::new);
    }

    public UserBadge getUserBadge(CreateUserRequestDto userDto) {
        SelenideElement root = allUsers.findBy(ownText(userDto.getUsername()));
        return new UserBadge(root);
    }
}
