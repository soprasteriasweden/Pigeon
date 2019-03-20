package se.soprasteria.automatedtesting.webdriver.helpers.utility.system;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.api.utility.SystemManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


public class PropertyHelper {

    public static Properties getProperties(Logger logger, String file) {
        Properties properties = new Properties();

        try {
            String pathToProperties = SystemManagement.resourceGetAbsolutePath(file);
            logger.trace("Using property file: " + pathToProperties);
            properties.load(new FileInputStream(pathToProperties));
        } catch (FileNotFoundException e) {
            String[] fixes = {"Verify that path to properties file is entered correctly in TestNG, " +
                    "please refer to reference implementation and documentation for more instructions"};
            throw new RuntimeException(Errors.getErrorMessage("Failed to find properties file", fixes));
        } catch (IOException e) {
            String[] fixes = {"Verify that the properties file is valid (standard java format)"};
            throw new RuntimeException(Errors.getErrorMessage("Found properties file but could not read it", fixes));
        }

        return properties;
    }

    public static void overrideSystemProperties(Logger logger, Properties properties) {
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            String key = (String) e.getKey();
            String systemPropertyValue = System.getProperty(key);
            if (systemPropertyValue != null) {
                logger.trace("Overriding system property " + key + ", new value: " + systemPropertyValue);
                properties.setProperty(key, systemPropertyValue);
            }
        }
    }

}
