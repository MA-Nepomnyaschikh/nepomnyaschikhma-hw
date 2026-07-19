package autotesting.practice_6.tests.ui.iteration_2;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.response.CreateAccountResponseDto;
import autotesting.practice_6.tests.BaseTest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Alert;

import java.util.Map;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.*;

public class DepositAccountTest extends BaseTest {

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
    public void userCanDepositAccountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        double amount = 5000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/deposit");

        $(".text-center").$("h1").shouldHave(Condition.text("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);

        $(".account-selector").selectOptionContainingText(userAccount.getAccountNumber());
        $(".deposit-input").setValue(valueOf(amount));

        $x("//button[text()='\uD83D\uDCB5 Deposit']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("✅ Successfully deposited $" + amount + " to account " + userAccount.getAccountNumber() + "!");
        alert.accept();

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(amount);
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(5000.01, "❌ Please deposit less or equal to 5000$."),
                Arguments.of(0, "❌ Please enter a valid amount."),
                Arguments.of(-0.01, "❌ Please enter a valid amount.")
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    public void userCannotDepositAccountWithInvalidAmountTest(double invalidAmount, String errorMessage) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/dashboard");

        $x("//button[text()='\uD83D\uDCB0 Deposit Money']").click();
        $(".text-center").$("h1").shouldHave(Condition.text("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);

        $(".account-selector").selectOptionContainingText(userAccount.getAccountNumber());
        $(".deposit-input").setValue(valueOf(invalidAmount));

        $x("//button[text()='\uD83D\uDCB5 Deposit']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains(errorMessage);
        alert.accept();

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
    }

    @Test
    public void userCannotDepositAccountWithoutAccountNumberTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        double amount = 5000.0;

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/dashboard");

        $x("//button[text()='\uD83D\uDCB0 Deposit Money']").click();
        $(".text-center").$("h1").shouldHave(Condition.text("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);

        $(".deposit-input").setValue(valueOf(amount));

        $x("//button[text()='\uD83D\uDCB5 Deposit']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please enter a valid amount.");
        alert.accept();

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
    }

    @Test
    public void userCannotDepositAccountWithoutAmountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);

        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', '" + token + "');");
        Selenide.open("/dashboard");

        $x("//button[text()='\uD83D\uDCB0 Deposit Money']").click();
        $(".text-center").$("h1").shouldHave(Condition.text("\uD83D\uDCB0 Deposit Money")).shouldBe(Condition.visible);

        $(".account-selector").selectOptionContainingText(userAccount.getAccountNumber());

        $x("//button[text()='\uD83D\uDCB5 Deposit']").click();

        Alert alert = switchTo().alert();
        softly.assertThat(alert.getText()).contains("❌ Please enter a valid amount.");
        alert.accept();

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
    }
}
