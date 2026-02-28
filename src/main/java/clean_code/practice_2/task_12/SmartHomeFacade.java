package clean_code.practice_2.task_12;

public class SmartHomeFacade {

    private final Light light;
    private final Conditioner conditioner;
    private final SecuritySystem securitySystem;

    public SmartHomeFacade(Light light, Conditioner conditioner, SecuritySystem securitySystem) {
        this.light = light;
        this.conditioner = conditioner;
        this.securitySystem = securitySystem;
    }

    public void startAllSystems() {
        System.out.println("Включаю все системы...");
        light.turnOn();
        conditioner.turnOn();
        securitySystem.turnOn();
    }

    public void stopAllSystems() {
        System.out.println("Выключаю все системы...");
        light.turnOff();
        conditioner.turnOff();
        securitySystem.turnOff();
    }

    public void leaveHome() {
        System.out.println("Хорошего дня!");
        light.turnOff();
        conditioner.turnOff();
        securitySystem.turnOn();
    }

    public void arriveHome() {
        System.out.println("Добро пожаловать домой!");
        light.turnOn();
        conditioner.turnOn();
        securitySystem.turnOff();
    }
}
