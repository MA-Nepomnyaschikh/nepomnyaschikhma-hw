package autotesting.practice_9.pages.elements;

import com.codeborne.selenide.SelenideElement;

public class UserBadge extends BaseElement {

    public UserBadge(SelenideElement root) {
        super(root);
    }

    public String getUsername() {
        return root.getOwnText().trim();
    }

    public String getRole() {
        return root.$(".badge").getText().trim();
    }
}
