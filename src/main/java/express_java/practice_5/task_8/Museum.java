package express_java.practice_5.task_8;

public class Museum {

    private Exhibit exhibit;

    public void setExhibit(Exhibit exhibit) {
        this.exhibit = exhibit;
    }

    public void maintenance() {
        exhibit.maintain();
    }

    public void showExhibit() {
        System.out.println(exhibit.getHistory());
    }
}
