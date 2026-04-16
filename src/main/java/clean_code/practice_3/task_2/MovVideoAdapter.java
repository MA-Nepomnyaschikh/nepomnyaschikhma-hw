package clean_code.practice_3.task_2;

public class MovVideoAdapter implements VideoAdapter {

    @Override
    public Video convertToMP4(Video video) {
        // Тут должна быть логика конвертации из MOV в MP4
        return new Video("MP4");
    }

    @Override
    public boolean supports(Video video) {
        return video.getFormat().equalsIgnoreCase("MOV");
    }
}
