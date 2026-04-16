package clean_code.practice_3.task_1;

/**
 * Разработка библиотеки для сокращения URL
 * Цель: Разработать библиотеку, которая предоставляет функциональность для сокращения длинных URL и их восстановления. Библиотека должна быть гибкой, чтобы в будущем можно было легко добавлять новые методы сокращения.
 *
 * Паттерны проектирования:
 * Singleton: Единственное хранилище всех URL.
 * Factory Method: Для создания объектов, отвечающих за различные методы сокращения URL.
 * Strategy: Для переключения между способами сокращения URL.
 *
 * Архитектура библиотеки:
 * UrlShortenerService: Класс, отвечающий за логику сокращения и восстановления URL.
 * UrlStorage: Интерфейс для хранения пар "длинный URL - короткий URL", с различными реализациями (например, память, файлы, базы данных).
 * ShorteningStrategy: Интерфейс, который определяет метод для сокращения URL. Реализации могут включать Base62, хеширование, UUID и другие.
 * ShortenerFactory: Класс, использующий Factory Method для создания объектов ShorteningStrategy.
 */

public class Main {
    public static void main(String[] args) {
        ShortenerFactory uuidStrategyFactory = new UUIDStrategyFactory();
        ShortenerFactory counterStrategyFactory = new CounterStrategyFactory();
        UrlStorage storage = InMemoryUrlStorage.getInstance();

        UrlShortenerService service = new UrlShortenerService(uuidStrategyFactory, storage);

        String shortUrl = service.shortenUrl("https://www.google.com");
        System.out.println("Short url with uuid strategy:" + shortUrl);

        String fullUrl = service.expandUrl(shortUrl);
        System.out.println("Expanded url with uuid strategy:" + fullUrl);

        UrlShortenerService service1 = new UrlShortenerService(counterStrategyFactory, storage);

        String shortUrl1 = service1.shortenUrl("https://www.chat-gpt.com");
        System.out.println("Short url with counter strategy:" + shortUrl1);

        String fullUrl1 = service1.expandUrl(shortUrl1);
        System.out.println("Expanded url with counter strategy:" + fullUrl1);
    }
}
