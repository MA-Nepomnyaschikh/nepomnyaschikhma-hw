package clean_code.practice_2.task_12;

public class Light {

    private boolean isActive;

    public void turnOn() {
        if (isActive) {
            throw new IllegalStateException("Свет уже включен");
        }
        isActive = true;
        System.out.println("Свет включен");
    }

    public void turnOff() {
        if (!isActive) {
            throw new IllegalStateException("Свет уже выключен");
        }
        isActive = false;
        System.out.println("Свет выключен");
    }
}
