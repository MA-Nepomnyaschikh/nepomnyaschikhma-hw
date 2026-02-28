package clean_code.practice_2.task_10;

public class Main {
    public static void main(String[] args) {
        KilometersDistance kilometersDistance = new KilometersDistance(10.5);
        kilometersDistance.getDistanceInKilometers();

        System.out.println();

        MilesAdapter milesDistance = new MilesAdapter(new MilesDistance(5.3));
        milesDistance.getDistanceInKilometers();
    }
}
