package org.example.ducksocialnetworkui.config;

import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILENAME = "config.properties";
    private static final Properties PROPERTIES = initProperties();

    private static Properties initProperties() {
        Properties properties = new Properties();

        try (var inputStream = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILENAME)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot load configuration properties stream.");
            }

            properties.load(inputStream);
            return properties;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot load configuration properties: ", e);
        }
    }

    public static Properties getConfig() {
        return PROPERTIES;
    }
}
