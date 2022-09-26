package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private static Properties prop() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
        properties.load(fileInputStream);
        return properties;
    }

    public static String getProperty(String propertyName) throws IOException {
        return prop().getProperty(propertyName);
    }
}
