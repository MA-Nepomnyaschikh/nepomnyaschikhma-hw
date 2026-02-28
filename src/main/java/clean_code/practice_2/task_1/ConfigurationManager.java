package clean_code.practice_2.task_1;

public class ConfigurationManager {
    private final String databaseConfig = "default database config";
    private final String sources = "default sources path";
    private final String logSettings = "default log settings";

    private static ConfigurationManager instance;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
}
