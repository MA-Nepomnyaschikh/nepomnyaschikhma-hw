package autotesting.practice_6.tests.ui.iteration_2;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.response.CreateAccountResponseDto;
import autotesting.practice_6.tests.BaseTest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import java.util.Map;

import static autotesting.practice_6.testdata.AccountData.MAX_TRANSFER_AMOUNT;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.valueOf;

public class TransferFundsTest extends BaseTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.baseUrl = "http://192.168.3.2:3000";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";

        Configuration.browserCapabilities.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true,
                        "enableLog", true)
        );
    }

    @Test
    public void userCanTransferFundsBetweenTheirAccountsTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(token, MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(token);
        
        double amount = 10000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(amount));
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("✅ Successfully transferred $" + amount + " to account " + receiverAccount.getAccountNumber() + "!");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(token, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - amount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(token, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(amount);
    }

    @Test
    public void userCanTransferFundsToAnotherUserAccountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);
        
        double amount = 10000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(amount));
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("✅ Successfully transferred $" + amount + " to account " + receiverAccount.getAccountNumber() + "!");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - amount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(amount);
    }

    @Test
    public void userCannotTransferFundsWithoutSenderAccountNumberTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double amount = 10000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(amount));
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please fill all fields and confirm.");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutReceiverAccountNumberTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double amount = 10000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(amount));
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please fill all fields and confirm.");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutAmountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please fill all fields and confirm.");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutConfirmTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double amount = 10000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(amount));

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please fill all fields and confirm.");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithInvalidAmountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double invalidAmount = 0.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + firstUserToken + "');");
        Selenide.open("/transfer");

        $("div.text-center").$("h1").shouldHave(Condition.text("\uD83D\uDD04 Make a Transfer")).shouldBe(Condition.visible);

        $("select.account-selector").selectOptionContainingText(senderAccount.getAccountNumber());
        $("input[placeholder='Enter recipient account number']").setValue(receiverAccount.getAccountNumber());
        $("input[placeholder='Enter amount']").setValue(valueOf(invalidAmount));
        $("input#confirmCheck").click();

        $x("//button[text()='\uD83D\uDE80 Send Transfer']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Error: Transfer amount must be at least 0.01");
        alert.accept();

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }
}
