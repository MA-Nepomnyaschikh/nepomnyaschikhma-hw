package express_java.practice_5.task_8;

public abstract class Exhibit {
    private String history;

    public Exhibit(String history) {
        this.history = history;
    }

    public String getHistory() {
        return history;
    }

    public abstract void maintain();
}
