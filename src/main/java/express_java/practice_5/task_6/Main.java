package express_java.practice_5.task_6;

public class Main {
    public static void main(String[] args) {
        Gardner gardner = new Gardner();
        Plant orchid = new Orchid();
        Plant cactus = new Cactus();
        gardner.setPlant(cactus);
        gardner.takeCare();
    }
}
