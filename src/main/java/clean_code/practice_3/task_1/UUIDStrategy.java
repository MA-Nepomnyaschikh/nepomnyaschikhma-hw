package clean_code.practice_3.task_1;

import java.util.UUID;

public class UUIDStrategy implements ShorteningStrategy {
    @Override
    public String shorten(String fullUrl) {
        return UUID.randomUUID().toString().substring(0, 8) + fullUrl.length();
    }
}
