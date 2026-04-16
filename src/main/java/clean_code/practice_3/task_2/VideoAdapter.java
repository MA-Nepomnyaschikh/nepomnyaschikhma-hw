package clean_code.practice_3.task_2;

public interface VideoAdapter {
    Video convertToMP4(Video video);
    boolean supports(Video video);
}
