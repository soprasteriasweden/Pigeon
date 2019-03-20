package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.DebugLevel;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.WebDriverType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;


public class WebDriverLog {

    private static final String SUREFIRE_FOLDER = "target/surefire-reports";
    private static AutomationDriver driver;
    private static BufferedReader continuousReader;
    private static String driverName;
    private static final List<WebDriverType> supportedDrivers = Arrays.asList(
            WebDriverType.ChromeDriver,
            WebDriverType.InternetExplorerDriver,
            WebDriverType.FirefoxDriver);

    public static LogEntries getLogEntries(AutomationDriver driver) {
        //LogEntry entry = new LogEntry(Level.ALL, new Timestamp(0).getTime(), "No logs available at this time. Supported WebDrivers is: AndroidDriver, ChromeDriver");
        if (driver.isInternetExplorer() || driver.isChrome() || driver.isAndroid()) {
            return driver.manage().logs().get(getLogType(driver));
        } else return null;
    }

    public static Boolean messageContainsInLog(AutomationDriver driver, String message) {
        for (LogEntry entry: driver.manage().logs().get(getLogType(driver))) {
            if (entry.getMessage().contains(message)) return true;
        }
        return false;
    }

    public static boolean driverLoggingEnabled() {
        DebugLevel.Level debugLevel = DebugLevel.getLevel();
        return debugLevel == DebugLevel.Level.DRIVERLOG
                || debugLevel == DebugLevel.Level.DRIVERLOG_AND_IMAGES
                || debugLevel == DebugLevel.Level.DRIVERLOG_AND_IMAGES_FAIL;
    }

    public static boolean supportedDriverType(AutomationDriver driver) {
        WebDriverType driverType = driver.getWebDriverType();
        return supportedDrivers.contains(driverType);
    }

    public static boolean driverLoggingOnPassEnabled() {
        DebugLevel.Level debugLevel = DebugLevel.getLevel();
        return debugLevel == DebugLevel.Level.DRIVERLOG || debugLevel == DebugLevel.Level.DRIVERLOG_AND_IMAGES;
    }

    public static boolean driverLoggingOnFailEnabled() {
        DebugLevel.Level debugLevel = DebugLevel.getLevel();
        return debugLevel == DebugLevel.Level.DRIVERLOG_AND_IMAGES_FAIL;
    }

    private static String getLogType(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return "logcat";
        }
        return "browser";
    }

    public static void printLogEntries(Logger logger) {

        if(supportedDriverType(driver)){
            LogEntries entries = WebDriverLog.getLogEntries(driver);
            for (LogEntry entry: entries.filter(Level.ALL)) {
                logger.info(String.format("LOG ENTRY - %S: %S", entry.getLevel(), entry.getMessage()));
            }
        } else {
            logger.info("Could not print WebDriver log entries, unsupported driver type");
        }

    }

    public static void printLogFile(Logger logger){
        if(driverLoggingEnabled() && supportedDriverType(driver)) {
            try {
                StringBuilder driverLog = new StringBuilder();
                String line = null;
                while((line = continuousReader.readLine()) != null) {
                    driverLog.append("\t\t\t" + line + "\n");
                }
                if(driverLog.length() > 1) {
                    logger.debug(driver.getWebDriverName().toUpperCase() +" LOG: \n" + driverLog);
                }
            } catch (Exception e) {
                logger.error("Could not print WebDriver logfile: " + e.getMessage());
            }
        } else if (driverLoggingEnabled() && !supportedDriverType(driver)) {
            logger.info("Could not print WebDriver logfile, unsupported driver type");
        }
    }

    public static void initializeWebDriverLog(AutomationDriver driver, Logger logger){
        if(driverLoggingEnabled()) {
            WebDriverLog.driver = driver;
            if(!supportedDriverType(WebDriverLog.driver)) throw new RuntimeException("Driver logging not supported for " +
                    WebDriverLog.driver.getWebDriverName() + ", currently supported driver types: " + supportedDrivers.toString());

            switch (WebDriverLog.driver.getWebDriverType()) {
                case ChromeDriver:
                    driverName = "chromedriver";
                    break;
                case InternetExplorerDriver:
                    driverName = "iedriver";
                    break;
                case FirefoxDriver:
                    driverName = "firefoxdriver";
                    break;
            }

            try {
                continuousReader = new BufferedReader(new FileReader(SUREFIRE_FOLDER + "/" + driverName + ".log"));
            } catch (Exception e) {
                logger.error("Framework could not find " + driverName + ".log at location" + SUREFIRE_FOLDER);
            }
        }
    }

    public static void clearWebDriverLog(Logger logger) {
        File logFile = new File(SUREFIRE_FOLDER + "/" + driverName + ".log");
        if(logFile.exists()) {
            try {
                continuousReader.close();
                System.gc();
                if(logFile.delete()) logger.info("Log file for " + driver.getWebDriverName() + " successfully deleted");
                else logger.info("Could not delete log file for " + driver.getWebDriverName());
            } catch (IOException e) {
                logger.error("Could not close file reader for log file");
            }
        }
    }
}
