package clean_code.practice_2.task_8;

public class Game {
    public static void main(String[] args) {
        Character character = new Character.Builder()
                .setHealth(10.0)
                .setDamage(2.0)
                .setArmor(2.0)
                .setMana(6.0)
                .build();

        System.out.println(character);
    }
}
