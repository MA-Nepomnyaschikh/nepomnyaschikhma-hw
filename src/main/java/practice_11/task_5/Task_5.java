package practice_11.task_5;

/**
 * Код должен увеличить возраст человека, но почему-то возраст остаётся прежним.
 */

public class Task_5 {
    public static void main(String[] args) {
        Person person = new Person("Alice", 25);
        updateAge(person);
        System.out.println("Updated age: " + person.getAge());
    }
    public static void updateAge(Person person) {
        person.setAge(person.getAge() + 1);
    }
}
