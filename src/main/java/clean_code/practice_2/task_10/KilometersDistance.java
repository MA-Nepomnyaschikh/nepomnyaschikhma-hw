package clean_code.practice_2.task_10;

import java.text.DecimalFormat;

public class KilometersDistance implements Distance {

    private double kilometers;

    public KilometersDistance(double kilometers) {
        this.kilometers = kilometers;
    }

    @Override
    public void getDistanceInKilometers() {
        DecimalFormat formatter = new DecimalFormat("#.#");
        System.out.println("Дистанция в километрах: " + formatter.format(kilometers));
    }

}
