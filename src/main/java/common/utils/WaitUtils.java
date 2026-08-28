package common.utils;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Function;

public final class WaitUtils {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private WaitUtils() {
    }

    public static <T> T waitUntil(Function<WebDriver, T> condition) {
        return Selenide.Wait()
                .withTimeout(DEFAULT_TIMEOUT)
                .until(condition);
    }

    public static <T> T waitUntil(Function<WebDriver, T> condition, Duration timeout) {
        return Selenide.Wait()
                .withTimeout(timeout)
                .until(condition);
    }
}
