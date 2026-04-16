package clean_code.practice_3.task_1;

public class UUIDStrategyFactory extends ShortenerFactory {
    @Override
    public ShorteningStrategy createStrategy() {
        return new UUIDStrategy();
    }
}
