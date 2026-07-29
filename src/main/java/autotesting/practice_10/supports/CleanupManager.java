package autotesting.practice_10.supports;

import java.util.ArrayDeque;
import java.util.Deque;

public class CleanupManager {
    private final Deque<Runnable> actions = new ArrayDeque<>();

    public void register(Runnable action) {
        actions.push(action);
    }

    public void cleanup() {
        while (!actions.isEmpty()) {
            try {
                actions.pop().run();
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
}
