package practice_5.task_6;

public class Gardner {

    private Plant plant;

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public void takeCare() {
        plant.care();
    }
}
