package clean_code.practice_3.task_1;

public class CounterStrategyFactory extends ShortenerFactory {
    @Override
    public ShorteningStrategy createStrategy() {
        return new CounterStrategy();
    }
}
