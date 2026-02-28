package clean_code.practice_2.task_12;

public class SecuritySystem {

    private boolean isActive;

    public void turnOn() {
        if (isActive) {
            throw new IllegalStateException("Система безопасности уже включена");
        }
        isActive = true;
        System.out.println("Система безопасности включена");
    }

    public void turnOff() {
        if (!isActive) {
            throw new IllegalStateException("Система безопасности уже выключена");
        }
        isActive = false;
        System.out.println("Система безопасности выключена");
    }
}
