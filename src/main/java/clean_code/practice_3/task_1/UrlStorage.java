package clean_code.practice_3.task_1;

public interface UrlStorage {
    String saveIfAbsent(String fullUrl, String shortUrl);
    String getFullUrl(String shortUrl);
    String getShortUrl(String fullUrl);
}
