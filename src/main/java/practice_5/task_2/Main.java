package practice_5.task_2;

public class Main {
    public static void main(String[] args) {
        Owner owner = new Owner();
        Pet cat = new Cat();
        Pet dog = new Dog();
        owner.setPet(dog);
        owner.interactWithPet();
        owner.feed();
    }
}
