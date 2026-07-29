package autotesting.practice_7.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement header = $(".text-center").$("h1");
    private final SelenideElement nameInput = $("input[placeholder='Enter new name']");
    private final SelenideElement saveChangeButton = $$("button").findBy(text("Save Changes"));
    private final SelenideElement logoutButton = $$("button").findBy(text("Logout"));

    @Override
    public String url() {
        return "/edit-profile";
    }

    @Override
    public ProfilePage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("✏\uFE0F Edit Profile"));
        return this;
    }

    public ProfilePage setNewName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage saveChanges() {
        saveChangeButton.click();
        return this;
    }

    public LoginPage logout() {
        logoutButton.click();
        return new LoginPage();
    }
}
