package ui.pages;

import com.codeborne.selenide.SelenideElement;
import api.models.response.CreateAccountResponseDto;

import java.math.BigDecimal;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class TransferPage extends BasePage<TransferPage> {
    private final SelenideElement header = $(".text-center").$("h1");
    private final SelenideElement senderAccountSelector = $("select.account-selector");
    private final SelenideElement receiverAccountNumber = $("input[placeholder='Enter recipient account number']");
    private final SelenideElement amountInput = $("input[placeholder='Enter amount']");
    private final SelenideElement confirmCheckbox = $("input#confirmCheck");
    private final SelenideElement transferButton =  $$("button").findBy(text("Send Transfer"));

    @Override
    public String url() {
        return "/transfer";
    }

    @Override
    public TransferPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining(url()));
        header.shouldBe(visible).shouldHave(text("\uD83D\uDD04 Make a Transfer"));
        return this;
    }

    public TransferPage selectSenderAccount(CreateAccountResponseDto senderAccount) {
        senderAccountSelector.shouldBe(visible,enabled);
        String accountNumber = senderAccount.getAccountNumber();

        shouldHaveOptionWithText(senderAccountSelector, accountNumber);

        senderAccountSelector.selectOptionContainingText(accountNumber);
        senderAccountSelector.getSelectedOption()
                .shouldHave(text(accountNumber));
        return this;
    }

    public TransferPage setReceiverAccount(CreateAccountResponseDto receiverAccount) {
        receiverAccountNumber.shouldBe(visible, enabled)
                .setValue(receiverAccount.getAccountNumber())
                .shouldHave(value(receiverAccount.getAccountNumber()));
        return this;
    }

    public TransferPage setAmount(BigDecimal amount) {
        amountInput.shouldBe(visible, enabled)
                .setValue(String.valueOf(amount))
                .shouldHave(value(String.valueOf(amount)));
        return this;
    }

    public TransferPage confirmDetails() {
        confirmCheckbox.shouldBe(visible, enabled).click();
        return this;
    }

    public TransferPage sendTransfer() {
        transferButton.shouldBe(visible, enabled).click();
        return this;
    }

    public TransferPage sendTransfer(CreateAccountResponseDto senderAccount, CreateAccountResponseDto receiverAccount, BigDecimal amount) {
        selectSenderAccount(senderAccount);
        setReceiverAccount(receiverAccount);
        setAmount(amount);
        confirmDetails();
        sendTransfer();
        return this;
    }
}
