package clean_code.practice_3.task_2;

import java.io.InputStream;

/**
 * Разработка библиотеки для стримингового сервиса видео
 * Цель: Создать библиотеку, которая обеспечивает функции загрузки и просмотра видео в форматах: AVI, MOV, WMV. Система должна поддерживать загрузку видео в различных форматах и конвертировать их в единый внутренний формат MP4 для упрощения стриминга.
 *
 * Паттерны проектирования:
 * Adapter: Для преобразования загружаемых видео форматов (AVI, MOV, WMV) в внутренний формат MP4.
 * Facade: Для предоставления упрощенного интерфейса к сложным операциям обработки видео, включая загрузку, конвертацию и стриминг.
 *
 * Архитектура библиотеки:
 * VideoService: Класс, использующий паттерн Facade, предоставляет методы uploadVideo и streamVideo, упрощая клиентский доступ к функционалу сервиса.
 * VideoAdapter: Интерфейс и его реализации для каждого поддерживаемого формата видео, преобразующие видео в формат MP4.
 */


public class Main {
    public static void main(String[] args) {
        VideoService videoService = new VideoService(
                new AviVideoAdapter(),
                new MovVideoAdapter(),
                new WmvVideoAdapter()
        );

        Video myVideo = new Video("AVI");
        Video uploadedVideo = videoService.uploadVideo(myVideo);
        System.out.println("Video uploaded successfully");

        InputStream stream = videoService.streamVideo(uploadedVideo.getId());
        System.out.println("Streaming video in format: " + uploadedVideo.getFormat() + ", with ID: " + uploadedVideo.getId());
    }
}
