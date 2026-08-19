package supports;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;

public class StepLogger {
    @FunctionalInterface
    public interface ThrowableRunnable<T> {
        T run() throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowableVoidRunnable {
        void run() throws Throwable;
    }

    public static <T> T log(String title, ThrowableRunnable<T> runnable) {
        return Allure.step(title, () -> {
            try {
                return runnable.run();
            } finally {
                if (WebDriverRunner.hasWebDriverStarted()) {
                    AllureAttachments.attachScreenshot();
                }
            }
        });
    }

    public static void log(String title, ThrowableVoidRunnable runnable) {
        Allure.step(title, () -> {
            try {
                runnable.run();
            } finally {
                if (WebDriverRunner.hasWebDriverStarted()) {
                    AllureAttachments.attachScreenshot();
                }
            }

            return null;
        });
    }
}
