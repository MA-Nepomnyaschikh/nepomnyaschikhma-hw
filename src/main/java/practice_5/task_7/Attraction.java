package main.java.practice_5.task_7;

public abstract class Attraction {
    private String name;

    public Attraction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void maintain();
}
