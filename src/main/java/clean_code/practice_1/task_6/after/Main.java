package clean_code.practice_1.task_6.after;

public class Main {
    public static void main(String[] args) {
        Penguin penguin = new Penguin();
        Sparrow sparrow = new Sparrow();
        penguin.eat();
        sparrow.eat();
        sparrow.fly();
    }
}
