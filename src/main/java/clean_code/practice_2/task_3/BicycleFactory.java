package clean_code.practice_2.task_3;

public class BicycleFactory extends TransportFactory {

    @Override
    Transport createTransport() {
        return new Bicycle();
    }
}
