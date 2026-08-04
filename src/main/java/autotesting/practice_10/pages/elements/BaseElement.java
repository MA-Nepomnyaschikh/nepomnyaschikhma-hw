package autotesting.practice_10.pages.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;

public abstract class BaseElement {
    protected final SelenideElement root;

    public BaseElement(SelenideElement root) {
        this.root = root;
    }

    public <T extends BaseElement> T shouldBeVisible() {
        root.shouldBe(visible);
        return (T) this;
    }

    public <T extends BaseElement> T shouldNotBeVisible() {
        root.shouldNotBe(visible);
        return (T) this;
    }

    protected SelenideElement find(By selector) {
        return root.find(selector);
    }

    protected SelenideElement find(String cssSelector) {
        return root.find(cssSelector);
    }

    protected ElementsCollection findAll(By selector) {
        return root.findAll(selector);
    }

    protected ElementsCollection findAll(String cssSelector) {
        return root.findAll(cssSelector);
    }
}
