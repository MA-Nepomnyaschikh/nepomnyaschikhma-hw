package express_java.practice_5.task_8;

public class Sculpture extends Exhibit {

    public Sculpture(String history) {
        super(history);
    }

    @Override
    public void maintain() {
        System.out.println("Осуществляется реставрация");
    }
}
