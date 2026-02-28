package clean_code.practice_2.task_12;

public class Conditioner {

    private boolean isActive;

    public void turnOn() {
        if (isActive) {
            throw new IllegalStateException("Кондиционер уже включен");
        }
        isActive = true;
        System.out.println("Кондиционер включен");
    }

    public void turnOff() {
        if (!isActive) {
            throw new IllegalStateException("Кондиционер уже выключен");
        }
        isActive = false;
        System.out.println("Кондиционер выключен");
    }
}
