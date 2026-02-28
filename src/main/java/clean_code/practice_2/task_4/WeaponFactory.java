package clean_code.practice_2.task_4;

public abstract class WeaponFactory {

    abstract Weapon createWeapon();

    public void useWeapon() {
        Weapon weapon = createWeapon();
        weapon.attack();
    }
}
