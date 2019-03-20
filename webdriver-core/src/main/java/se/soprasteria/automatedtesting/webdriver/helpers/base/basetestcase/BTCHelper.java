package se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.Property;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.Config;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.Setup;

import javax.annotation.Nullable;
import java.util.*;


public class BTCHelper {

    private static ArrayList<WebDriver> webDriversOpenedDuringSession = new ArrayList<>();

    public static AutomationDriver getDriver(Logger logger, String webdriverConfigId) {
        DriverConfig driverConfiguration = getDriverConfigurations(logger, webdriverConfigId).get(0);
        AutomationDriver driver = new Setup(driverConfiguration, "", "").getWebDriver();
        webDriversOpenedDuringSession.add(driver);
        return driver;
    }

    public static void closeWebDriversOpenedDuringSession(Logger logger){
        int nrOfOpenedWebDrivers = webDriversOpenedDuringSession.size();
        if(nrOfOpenedWebDrivers > 0) {
            String wD = "WEBDRIVER";
            if(nrOfOpenedWebDrivers > 1) wD += "S";
            logger.info("CLOSING " + nrOfOpenedWebDrivers + " ADDITIONAL " + wD + " OPENED BY CONFIGURATION ID");
            for(WebDriver webDriver : webDriversOpenedDuringSession) {
                webDriver.quit();
            }
        }
        webDriversOpenedDuringSession.clear();
    }

    public static Object[][] getWebDriversForDataProvider(List<DriverConfig> driverConfigurations,
                                                          String testSuiteName, String testMethodName) {
        List<Object[]> drivers = new ArrayList<>();
        for (DriverConfig driverConfiguration: driverConfigurations) {
            Object[] driver = {new Setup(driverConfiguration, testSuiteName, testMethodName).getWebDriver()};
            drivers.add(driver);
        }
        return drivers.toArray(new Object[drivers.size()][]);
    }

    public static List<DriverConfig> getDriverConfigurations(Logger logger, String webdriverConfigId){
        List<DriverConfig> driverConfigurations = new ArrayList<>();

        String[] configs = webdriverConfigId.split("\\s+");
        logger.info("configId: "+ webdriverConfigId);
        List<DriverConfig> driverConfigs =
                BaseTestConfig.getInstance().getConfig().webdriverConfigurations;

        for (DriverConfig config: driverConfigs) {
            if (Arrays.asList(configs).contains(config.id)) {
                driverConfigurations.add(config);
            }
        }

        if (driverConfigurations.size() < 1) {
            String[] fixes = {"Verify that the config id is correct and exists in the config XML"};
            throw new RuntimeException(
                    Errors.getErrorMessage("Failed to find any matching webdrivers for the ID in the properties file",
                    fixes));
        }

        return driverConfigurations;
    }

    public static String getConfigurationUrlPort(Logger logger, String webdriverConfigId) {
        List<DriverConfig> driverConfigurations = new ArrayList<>();

        String[] configs = webdriverConfigId.split("\\s+");
        List<DriverConfig> driverConfigs =
                BaseTestConfig.getInstance().getConfig().webdriverConfigurations;

        for (DriverConfig config: driverConfigs) {
            if (Arrays.asList(configs).contains(config.id)) {
                if(config.url != null) {
                    return config.url.split(":")[2].split("/")[0];
                } else {
                    throw new RuntimeException("Configuration Id " + webdriverConfigId + " does not contain an url, cannot " +
                            "extract port");
                }
             }
        }

        String[] fixes = {"Verify that the config id is correct and exists in the config XML"};
        throw new RuntimeException(
                Errors.getErrorMessage("Failed to find any matching webdrivers for the ID in the properties file",
                        fixes));
    }

    public static boolean doesConfigurationTypeSupportAppium(Logger logger, String webdriverConfigId) {
        List<DriverConfig> driverConfigurations = new ArrayList<>();

        String[] configs = webdriverConfigId.split("\\s+");
        List<DriverConfig> driverConfigs =
                BaseTestConfig.getInstance().getConfig().webdriverConfigurations;

        for (DriverConfig config: driverConfigs) {
            if (Arrays.asList(configs).contains(config.id)) {
                return config.type.equals("AndroidDriver") | config.type.equals("IOSDriver") | config.type.equals("WindowsDriver");
            }
        }

        String[] fixes = {"Verify that the config id is correct and exists in the config XML"};
        throw new RuntimeException(
                Errors.getErrorMessage("Failed to find any matching webdrivers for the ID in the properties file",
                        fixes));

    }

    public static Map<String, Object> getData(Logger logger) {
        Map<String, Object> data = new HashMap<String, Object>();
        Config config = BaseTestConfig.getInstance().getConfig();
        List<Property> properties = config.properties;

        if (properties != null) {
            for (Property property : properties)
                if (property.name.startsWith("test.data")) {
                    String[] segments = property.name.split("\\.");
                    String type = segments[2];
                    String value = config.getProperty(property.name);
                    data.put(property.name, getValueFromProperty(logger, type, value));
                }
        }
        return data;
    }

    private static Object getValueFromProperty(Logger logger, String type, String value) {
        switch (type) {
            case "int":
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    logger.error("Failed to parse integer from property due to invalid value or type set in property name");
                    throw e;
                }

            case "csv":
                return value.split(",");
            case "string":
                return value;
            default:
                throw new RuntimeException("Failed to parse property due to invalid type: " + type + ", please verify " +
                        "values in properties file");

        }
    }

    @Nullable
    public static Object[][] addTestData(Logger logger, Object[][] data, Object val) {
        Errors.throwExceptionIfNull(data, "CRITICAL: Dataprovider was not initialized properly");
        if (val == null) {
            String[] fixes = {
                "enter a valid key in the testcase when overriding testDataKey"
            };
            Errors.logError(logger, "Failed to find any data to add to dataprovider", fixes);
            return null;
        }

        Object[] valuesToAdd = new Object[]{val};
        List <Object[]> newList = new ArrayList<Object[]>();

        // For every data in the list, add all the values we have specified
        for (int i=0; i < data.length; i++) {
            for (int j=0; j < valuesToAdd.length; j++) {
                List<Object> current = new ArrayList<Object>(Arrays.asList(data[i]));
                current.add(valuesToAdd[j]);
                newList.add(current.toArray());
            }
        }

        return (Object[][]) newList.toArray(new Object[newList.size()][]);
    }
}
