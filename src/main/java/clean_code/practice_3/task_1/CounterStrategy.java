package clean_code.practice_3.task_1;

import java.util.concurrent.atomic.AtomicLong;

public class CounterStrategy implements ShorteningStrategy {
    private static final AtomicLong COUNTER = new AtomicLong();

    @Override
    public String shorten(String fullUrl) {
        StringBuilder sb = new StringBuilder();
        return sb.append(COUNTER.getAndIncrement()).append(fullUrl.length()).toString();
    }
}
