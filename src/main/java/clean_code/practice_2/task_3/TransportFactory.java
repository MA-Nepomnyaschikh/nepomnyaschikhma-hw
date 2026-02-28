package clean_code.practice_2.task_3;

public abstract class TransportFactory {

    abstract Transport createTransport();

    public void startTrip() {
        Transport transport = createTransport();
        transport.drive();
    }
}
