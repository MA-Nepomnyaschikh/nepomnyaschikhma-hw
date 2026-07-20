package autotesting.practice_7.pages;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

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

    public AdminPanelPage shouldHaveUserInUsersList(CreateUserRequestDto userDto) {
        getAllUsers().findBy(exactText(userDto.getUsername() + "\n" + userDto.getRole())).shouldBe(visible);
        return this;
    }

    public AdminPanelPage shouldNotHaveUserInUsersList(CreateUserRequestDto userDto) {
        getAllUsers().findBy(exactText(userDto.getUsername() + "\n" + userDto.getRole())).shouldNotBe(visible);
        return this;
    }
}
