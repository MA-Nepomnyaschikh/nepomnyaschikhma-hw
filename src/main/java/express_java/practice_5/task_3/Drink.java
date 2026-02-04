package express_java.practice_5.task_3;

public class Drink extends Dish {
    private int volume;

    public Drink(int volume) {
        this.volume = volume;
    }

    @Override
    public void printInfo() {
        System.out.println("Объем напитка: " + volume + " миллилитров");
    }
}
