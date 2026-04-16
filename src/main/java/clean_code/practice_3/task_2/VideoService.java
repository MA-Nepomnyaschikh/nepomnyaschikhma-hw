package clean_code.practice_3.task_2;

import clean_code.practice_3.task_2.exceptions.UnsupportedFormatException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VideoService {

    private final List<VideoAdapter> adapters;
    private final HashMap<String, Video> videos = new HashMap<>();

    public VideoService(VideoAdapter... adapters) {
        this.adapters = Arrays.asList(adapters);
    }

    public Video uploadVideo(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null");
        }

        for (VideoAdapter adapter : adapters) {
            if (adapter.supports(video)) {
                Video convertedVideo = adapter.convertToMP4(video);

                String id = UUID.randomUUID().toString();
                String filePath = "/storage/" + id + ".mp4";
                convertedVideo.setId(id);
                convertedVideo.setFilePath(filePath);

                videos.put(id, convertedVideo);
                return convertedVideo;
            }
        }
        throw new UnsupportedFormatException("Unsupported format");
    }

    public InputStream streamVideo(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Video video = videos.get(id);

        if (video == null) {
            throw new RuntimeException("Video not found");
        }

        String fakeData = video.getFilePath();
        return new ByteArrayInputStream(fakeData.getBytes());
    }
}
