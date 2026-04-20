package clean_code.practice_3.task_1;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlStorage implements UrlStorage {

    private static final InMemoryUrlStorage INSTANCE = new InMemoryUrlStorage();
    private final ConcurrentHashMap<String,String> fullToShortStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,String> shortToFullStorage = new ConcurrentHashMap<>();

    private InMemoryUrlStorage() {}

    public static InMemoryUrlStorage getInstance() {
        return INSTANCE;
    }

    @Override
    public String saveIfAbsent(String fullUrl, String shortUrl) {
        String existingShortUrl = fullToShortStorage.putIfAbsent(fullUrl, shortUrl);

        if (existingShortUrl != null) {
            return existingShortUrl;
        }

        shortToFullStorage.put(shortUrl, fullUrl);
        return shortUrl;
    }

    @Override
    public String getFullUrl(String shortUrl) {
        return shortToFullStorage.get(shortUrl);
    }

    @Override
    public String getShortUrl(String fullUrl) {
        return fullToShortStorage.get(fullUrl);
    }
}
