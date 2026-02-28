package clean_code.practice_2.task_4;

public class Main {
    public static void main(String[] args) {
        WeaponFactory factory = new BowFactory();
        factory.useWeapon();

        factory = new GunFactory();
        factory.useWeapon();

        factory = new SwordFactory();
        factory.useWeapon();
    }
}
