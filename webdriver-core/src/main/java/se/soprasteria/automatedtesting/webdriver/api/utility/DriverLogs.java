package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.WebDriverLog;


public class DriverLogs {

    /**
     * Get all browser log gathered with given webdriver.
     * NOTE: Logs will be consumed as method is called. Subsequent calls will return new logs gathered since last call.
     *
     * @param driver to use and get browser logs out of
     * @return all LogEntries gathered up to this point.
     */
    public static LogEntries getEntries(AutomationDriver driver) { return WebDriverLog.getLogEntries(driver); }

    /**
     * Will search through logs and search for string param message. If message contains in a LogEntry, returns true. Else false.
     * NOTE: Logs will be consumed as method is called. Subsequent calls will only return new logs gathered since last call.
     *
     * @param driver to use and go through log messages with
     * @param message to search through log entries with.
     * @return true if param 'message' contains in any LogEntry
     */
    public static boolean doesMessageContainsInLogEntries(AutomationDriver driver, String message) { return WebDriverLog.messageContainsInLog(driver, message); }
}
