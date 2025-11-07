package practice_5.task_4;

public class Acuarium {

    private SeaCreature seaCreature;

    public void setSeaCreature(SeaCreature seaCreature) {
        this.seaCreature = seaCreature;
    }

    public void showBehavior() {
        seaCreature.move();
    }
}
