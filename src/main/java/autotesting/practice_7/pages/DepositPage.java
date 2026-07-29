package autotesting.practice_7.pages;

import autotesting.practice_7.models.response.CreateAccountResponseDto;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class DepositPage extends BasePage<DepositPage> {
    private final SelenideElement header = $(".text-center").$("h1");
    private final SelenideElement accountSelector = $("select.account-selector");
    private final SelenideElement amountInput = $("input.deposit-input");
    private final SelenideElement depositButton = $$("button").findBy(text("Deposit"));;

    @Override
    public String url() {
        return "/deposit";
    }

    @Override
    public DepositPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("\uD83D\uDCB0 Deposit Money"));
        return this;
    }

    public DepositPage selectAccount(CreateAccountResponseDto userAccount) {
        accountSelector.selectOptionContainingText(userAccount.getAccountNumber());
        return this;
    }

    public DepositPage setAmount(double amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    public DepositPage sendDeposit() {
        depositButton.click();
        return this;
    }

    public DepositPage sendDeposit(CreateAccountResponseDto userAccount, double amount) {
        selectAccount(userAccount);
        setAmount(amount);
        depositButton.click();
        return this;
    }
}
