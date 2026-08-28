package common.cleanup;

import common.allure.StepLogger;

import java.util.ArrayDeque;
import java.util.Deque;

public class CleanupManager {
    private final Deque<Runnable> actions = new ArrayDeque<>();

    public void register(Runnable action) {
        actions.push(action);
    }

    public void cleanup() {
        while (!actions.isEmpty()) {
            StepLogger.apiStep("Удалить пользователя", () -> {
                try {
                    actions.pop().run();
                } catch (Exception e) {
                    System.err.println("Cleanup failed: " + e.getMessage());
                }
            });
        }
    }
}
