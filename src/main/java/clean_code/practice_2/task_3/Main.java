package clean_code.practice_2.task_3;

public class Main {
    public static void main(String[] args) {
        TransportFactory garage = new CarFactory();
        garage.startTrip();

        garage = new BicycleFactory();
        garage.startTrip();
    }
}
