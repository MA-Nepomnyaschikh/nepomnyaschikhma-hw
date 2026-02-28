package clean_code.practice_2.task_2;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final List<String> eventLogs = new ArrayList<>();
    private final List<String> errorLogs = new ArrayList<>();
    private final List<String> warningLogs = new ArrayList<>();

    private static Logger instance;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logEvent(String message) {
        eventLogs.add("[INFO]" + message);
    }

    public void logEeeor(String message) {
        errorLogs.add("[ERROR]" + message);
    }

    public void logWarning(String message) {
        warningLogs.add("[WARNING]" + message);
    }
}
