package practice_5.task_7;

public class RollerCoaster extends Attraction {

    public RollerCoaster(String name) {
        super(name);
    }

    @Override
    public void maintain() {
        System.out.println("Проверка безопасности");
    }
}
