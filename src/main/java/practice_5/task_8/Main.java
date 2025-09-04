package main.java.practice_5.task_8;

public class Main {
    public static void main(String[] args) {
        Museum museum = new Museum();
        Exhibit manuscript = new Manuscript("Этот манускрипт был написан в Древнем Египте");
        Exhibit sculpture = new Sculpture("Эта скульптура была создана в Древней Греции");
        museum.setExhibit(manuscript);
        museum.maintenance();
        museum.showExhibit();
    }
}
