package main.java.practice_5.task_7;

import java.util.ArrayList;

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
