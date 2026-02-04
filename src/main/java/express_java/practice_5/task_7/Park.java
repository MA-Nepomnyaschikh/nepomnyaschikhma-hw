package express_java.practice_5.task_7;

public class Park {
    private Attraction attraction;

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public void printAttractionInfo() {
        System.out.println(attraction.getName());
    }

    public void maintenance() {
        attraction.maintain();
    }
}
