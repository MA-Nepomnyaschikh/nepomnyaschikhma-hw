package clean_code.practice_2.task_8;

public class Character {
    private double health;
    private double damage;
    private double armor;
    private double mana;

    private Character(Builder builder) {
        this.health = builder.health;
        this.damage = builder.damage;
        this.armor = builder.armor;
        this.mana = builder.mana;
    }

    @Override
    public String toString() {
        return "Character: " +
                "health: " + health +
                ", damage: " + damage +
                ", armor: " + armor +
                ", mana: " + mana;
    }

    static class Builder {
        private double health;
        private double damage;
        private double armor;
        private double mana;

        public Builder setHealth(double health) {
            this.health = health;
            return this;
        }

        public Builder setDamage(double damage) {
            this.damage = damage;
            return this;
        }

        public Builder setArmor(double armor) {
            this.armor = armor;
            return this;
        }

        public Builder setMana(double mana) {
            this.mana = mana;
            return this;
        }

        public Character build() {
            return new Character(this);
        }
    }
}
