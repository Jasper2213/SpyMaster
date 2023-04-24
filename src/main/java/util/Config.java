package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static final Config INSTANCE = new Config("/config/config.properties");

    private final Properties properties;

    public static Config getInstance() {
        return INSTANCE;
    }

    private Config(String path) {
        properties = new Properties();

        try (InputStream is = Config.class.getResourceAsStream(path)) {
            properties.load(is);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load config", ex);
            throw new SpyMasterException("Unable to load configuration.");
        }
    }

    public static String get(String key) {
        return getInstance().readSetting(key);
    }

    public String readSetting(String key) {
        return readSetting(key, null);
    }

    public String readSetting(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
