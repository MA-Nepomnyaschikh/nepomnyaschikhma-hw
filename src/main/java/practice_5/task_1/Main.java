package practice_5.task_1;

public class Main {
    public static void main(String[] args) {
        Zoopark zoo = new Zoopark();
        Animal bird = new Bird();
        Animal elephant = new Elephant();
        zoo.setAnimal(elephant);
        zoo.showBehavior();

    }
}
