package main.java.practice_5.task_5;

public class Farm {

    private Animal animal;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void feed() {
        animal.eat();
    }

    public void collectProduce() {
        animal.produce();
    }
}
