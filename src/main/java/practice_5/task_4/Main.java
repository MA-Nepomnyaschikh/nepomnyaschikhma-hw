package main.java.practice_5.task_4;

public class Main {
    public static void main(String[] args) {
        Acuarium acuarium = new Acuarium();
        SeaCreature starfish = new Starfish();
        SeaCreature shark = new Shark();
        acuarium.setSeaCreature(shark);
        acuarium.showBehavior();
    }
}
