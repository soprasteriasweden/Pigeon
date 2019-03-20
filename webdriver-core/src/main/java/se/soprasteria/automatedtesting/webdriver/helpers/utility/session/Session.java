package se.soprasteria.automatedtesting.webdriver.helpers.utility.session;

import org.apache.logging.log4j.LogManager;
import org.testng.ITestResult;
import org.testng.Reporter;
import se.soprasteria.automatedtesting.webdriver.api.utility.DebugImage;
import se.soprasteria.automatedtesting.webdriver.api.utility.Time;

public class Session {

    private static String currentConfigurationId = "NoConfigurationIdSet";

    /**
     * Returns a unique identifier for a test method in the ongoing session. The identifier is based on the current
     * method name, the configuration ID used and the current date and time.
     *
     * @param result - An ITestResult to extract the data from, in case of null we will try to get one using Reporter
     * @return A unique identifier for a test method in the ongoing session.
     */
    public static String getTestMethodID(ITestResult result) {
        if (result == null) {
            result = Reporter.getCurrentTestResult();
        }

        if (result == null) {
            LogManager.getLogger(DebugImage.class).trace("Failed to getTestMethodID using testresult");
            return null;
        }
        String testMethod = result.getName();
        return String.format("%s__%s__%s" , testMethod, getCurrentConfigurationId(), Time.getCurrentTestStartTime(result));
    }

    /**
     * @return Returns the current configuration ID as a String
     */
    public static String getCurrentConfigurationId(){return currentConfigurationId;}

    /**
     * Sets the current configuration ID. NOTE: This is set automatically at the start of every test suite
     * and should not be set manually if without specific cause.
     *
     * @param configurationId Current configuration ID
     */
    public static void setCurrentConfigurationId(String configurationId) {currentConfigurationId = configurationId;}
}
