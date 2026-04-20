package clean_code.practice_3.task_1;

import clean_code.practice_3.task_1.validators.UrlValidator;

public class UrlShortenerService {
    private final ShorteningStrategy shorteningStrategy;
    private final UrlStorage storage;

    public UrlShortenerService(ShortenerFactory factory, UrlStorage storage) {
        this.shorteningStrategy = factory.createStrategy();
        this.storage = storage;
    }

    public String shortenUrl(String fullUrl) {
        UrlValidator.validateFullUrl(fullUrl);

        String existingShortUrl = storage.getShortUrl(fullUrl);
        if (existingShortUrl != null) {
            return existingShortUrl;
        }

        String shortUrl = shorteningStrategy.shorten(fullUrl);
        return storage.saveIfAbsent(fullUrl, shortUrl);
    }

    public String expandUrl(String shortUrl) {
        UrlValidator.validateShortUrl(shortUrl);

        String fullUrl = storage.getFullUrl(shortUrl);
        UrlValidator.validateNotBlank(fullUrl);

        return fullUrl;
    }
}
