package clean_code.practice_2.task_12;

public class Main {
    public static void main(String[] args) {
        Light light = new Light();
        Conditioner conditioner = new Conditioner();
        SecuritySystem securitySystem = new SecuritySystem();

        SmartHomeFacade smartHome = new SmartHomeFacade(light, conditioner, securitySystem);
        smartHome.startAllSystems();
    }
}
