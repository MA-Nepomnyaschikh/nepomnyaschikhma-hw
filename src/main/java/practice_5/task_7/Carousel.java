package practice_5.task_7;

public class Carousel extends Attraction {

    public Carousel(String name) {
        super(name);
    }

    @Override
    public void maintain() {
        System.out.println("Техническое обслуживание");
    }
}
