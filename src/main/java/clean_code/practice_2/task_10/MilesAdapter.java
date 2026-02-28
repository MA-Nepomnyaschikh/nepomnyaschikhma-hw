package clean_code.practice_2.task_10;

import java.text.DecimalFormat;

public class MilesAdapter implements Distance {

    private MilesDistance milesDistance;

    public MilesAdapter(MilesDistance milesDistance) {
        this.milesDistance = milesDistance;
    }

    @Override
    public void getDistanceInKilometers() {
        double miles = milesDistance.getMiles();
        double kilometers = miles * 1.60934;
        DecimalFormat formatter = new DecimalFormat("#.#");
        System.out.println("Дистанция в километрах: " + formatter.format(kilometers));
    }
}
