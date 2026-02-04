package express_java.practice_5.task_8;

public class Manuscript extends Exhibit {

    public Manuscript(String history) {
        super(history);
    }

    @Override
    public void maintain() {
        System.out.println("Осуществляется контроль температуры и влажности");
    }
}
