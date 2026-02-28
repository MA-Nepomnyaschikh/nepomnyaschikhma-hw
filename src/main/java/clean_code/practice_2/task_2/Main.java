package clean_code.practice_2.task_2;

public class Main {
    public static void main(String[] args) {
        Logger logger_1 = Logger.getInstance();
        Logger logger_2 = Logger.getInstance();

        // ССылки указывают на один и тот же объект, потому что Logger является синглотоном
        System.out.println(logger_1 == logger_2);
    }
}
