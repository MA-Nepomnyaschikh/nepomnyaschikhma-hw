package practice_5.task_7;

public class Main {
    public static void main(String[] args) {
        Park park = new Park();
        Attraction carousel = new Carousel("Карусель");
        Attraction rollerCoaster = new RollerCoaster("Американские горки");
        park.setAttraction(rollerCoaster);
        park.maintenance();
        park.printAttractionInfo();
    }
}
