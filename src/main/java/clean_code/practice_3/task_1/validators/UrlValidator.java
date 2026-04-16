package clean_code.practice_3.task_1.validators;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlValidator {

    public static void validateNotBlank(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("Url is null or empty");
        }
    }

    public static void validateFullUrl(String url) {
        validateNotBlank(url);

        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("Invalid full url format");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid full url format", e);
        }
    }

    public static void validateShortUrl(String url) {
        validateNotBlank(url);

        if (!url.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Invalid short url format");
        }
    }
}
