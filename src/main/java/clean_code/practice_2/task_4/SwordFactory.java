package clean_code.practice_2.task_4;

public class SwordFactory extends WeaponFactory {

    @Override
    Weapon createWeapon() {
        return new Sword();
    }
}
