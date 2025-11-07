package practice_5.task_1;

public class Zoopark {

    private Animal animal;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void showBehavior() {
        animal.makeSound();
        animal.move();
    }
}
