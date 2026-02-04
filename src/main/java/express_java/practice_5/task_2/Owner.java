package express_java.practice_5.task_2;

public class Owner {

    private Pet pet;

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void interactWithPet() {
        pet.interact();
    }

    public void feed() {
        pet.eat();
    }
}
