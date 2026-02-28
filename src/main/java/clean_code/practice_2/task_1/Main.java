package clean_code.practice_2.task_1;

public class Main {
    public static void main(String[] args) {
        ConfigurationManager configurationManager_1 = ConfigurationManager.getInstance();
        ConfigurationManager configurationManager_2 = ConfigurationManager.getInstance();

        // ССылки указывают на один и тот же объект, потому что ConfigurationManager является синглотоном
        System.out.println(configurationManager_1 == configurationManager_2);
    }
}
