package clean_code.practice_2.task_4;

public class Gun implements Weapon {
    @Override
    public void attack() {
        System.out.println("Выстрел из пистолета");
    }
}
