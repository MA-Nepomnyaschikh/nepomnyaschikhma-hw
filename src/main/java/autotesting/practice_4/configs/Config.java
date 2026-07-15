package autotesting.practice_4.configs;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();

    private final Properties properties = new Properties();

    private Config() {
        try (InputStream input = Config.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException("Fail to load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return INSTANCE.properties.getProperty(key);
    }

    public static Config getInstance() {
        return INSTANCE;
    }
}
