package configs;

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
        // Первый приоритет - Системная переменная
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        // Второй приоритет - Переменная окружения
        String envKey = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null) {
            return envValue;
        }
        // Третий приоритет - config.properties
        return INSTANCE.properties.getProperty(key);
    }
}
