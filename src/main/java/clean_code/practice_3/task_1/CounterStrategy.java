package clean_code.practice_3.task_1;

import java.util.concurrent.atomic.AtomicLong;

public class CounterStrategy implements ShorteningStrategy {
    private static final AtomicLong COUNTER = new AtomicLong();

    @Override
    public String shorten(String fullUrl) {
        return "" + COUNTER.getAndIncrement() + fullUrl.length();
    }
}
