package clean_code.practice_1.task_7.after;

public class Main {
    public static void main(String[] args) {
        Programmer programmer = new Programmer();
        Director director = new Director();
        programmer.work();
        director.work();
        director.eat();
    }
}
