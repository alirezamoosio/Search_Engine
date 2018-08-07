package ir.nimbo.searchengine.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance = new ConfigManager();
    private Properties properties = new Properties();

    public static ConfigManager getInstance() {
        return instance;
    }

    private ConfigManager() {
        try {
            properties.load(new FileInputStream(getClass().getClassLoader().getResource("config.properties").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(PropertyType type) {
        return properties.getProperty(type.toString());
    }

}
