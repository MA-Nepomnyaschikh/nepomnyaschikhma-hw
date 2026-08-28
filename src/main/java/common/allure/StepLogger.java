package common.allure;

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

    public static <T> T uiStep(String title, ThrowableRunnable<T> runnable) {
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

    public static void uiStep(String title, ThrowableVoidRunnable runnable) {
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

    public static <T> T apiStep(String title, ThrowableRunnable<T> runnable) {
        return Allure.step(title, runnable::run);
    }

    public static void apiStep(String title, ThrowableVoidRunnable runnable) {
        Allure.step(title, () -> {
            runnable.run();
            return null;
        });
    }
}
