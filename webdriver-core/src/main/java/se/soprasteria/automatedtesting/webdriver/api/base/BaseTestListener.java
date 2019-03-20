package se.soprasteria.automatedtesting.webdriver.api.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.DebugLevel;
import se.soprasteria.automatedtesting.webdriver.api.utility.DebugImage;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.api.utility.QAColors;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestlistener.BTLHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.Screenshot;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.WebDriverLog;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.Session;
import se.soprasteria.automatedtesting.webdriver.helpers.video.VideoRecording;


/**
 * TestNG listener events. Used to automatically help with debugging and diagnosing failed tests. The listener is specified
 * in the testng XML with the 'listener' tag. This implements ITestListener.
 */
public class BaseTestListener extends BaseClass implements ITestListener {
    /**
     * Invoked after the test class is instantiated and before any configuration method is called.
     *
     * @param context Contains all the information available on the test about to run.
     */
    public void onStart(ITestContext context) {
        logger.info("STARTING TESTSUITE: " + context.getName());
    }

    /**
     * Invoked after all the tests have run and all their Configuration methods have been called.
     *
     * @param context Contains all the information available on the test about to run.
     */
    public void onFinish(ITestContext context) {
        logger.info("FINISHED TESTSUITE: " + context.getName());
    }

    /**
     * Invoked each time before a test will be invoked. The ITestResult is only partially filled with the references to
     * class, method, start millis and status.
     *
     * @param result the partially filled ITestResult.
     */
    public void onTestStart(ITestResult result) {
        if (result.getStatus() == ITestResult.STARTED) {
            logger.info("TEST STARTED: " + result.getName());
        }
    }

    private AutomationDriver getDriver(ITestResult result) {
        onTestStart(result);
        Object[] params = result.getParameters();
        WebDriver driver;
        if (params[0] instanceof EventFiringWebDriver) {
            EventFiringWebDriver eventFiringWebDriver = (EventFiringWebDriver) params[0];
            driver = eventFiringWebDriver.getWrappedDriver();
        } else driver = (AutomationDriver) params[0];
        return  (AutomationDriver) driver;
    }

    private boolean isScreenshotOnFailEnabled() {
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        return currentLevel != DebugLevel.Level.CONSOLE &&
                currentLevel != DebugLevel.Level.DRIVERLOG;
    }

    private boolean isScreenshotOnSuccessEnabled() {
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        return isScreenshotOnFailEnabled() &&
                currentLevel != DebugLevel.Level.IMAGES_FAIL &&
                currentLevel != DebugLevel.Level.DRIVERLOG_AND_IMAGES_FAIL;
    }

    private boolean driverLoggingOnFailEnabled() {
        return WebDriverLog.driverLoggingOnFailEnabled();
    }

    /**
     * Captures screenshot and saves the pagesource when a test fails.
     *
     * @param result ITestResult containing information about the run test.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testmethod = result.getName();
        if (result.getParameters().length != 0) {
            if (isScreenshotOnFailEnabled()) {
                try {
                    DebugImage.appendImage(
                            logger,
                            Session.getTestMethodID(result),
                            Screenshot.captureScreenshot(),
                            "FAIL: " + result.getName(),
                            QAColors.getColor(QAColors.State.FAIL));
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot on test fail");
                }
            }
            BTLHelper.savePageSource(logger, result);
        }
        stopVideoRecording(result.getName(), false, result);
        logger.warn("TEST FAILED: " + testmethod + "\n");
    }

    /**
     * Captures screenshot when the test has passed correctly.
     *
     * @param result ITestResult containing information about the run test.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testmethod = result.getName();
        if (result.getParameters().length != 0) {
            if (isScreenshotOnSuccessEnabled()) {
                try {
                    DebugImage.appendImage(
                            logger,
                            Session.getTestMethodID(result),
                            Screenshot.captureScreenshot(),
                            "PASS:" + result.getName(),
                            QAColors.getColor(QAColors.State.PASS));
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot on test pass");
                }
            }
        }
        stopVideoRecording(result.getName(), true, result);
        logger.info("TEST PASSED: " + testmethod + "\n");
    }

    public void stopVideoRecording(String testcase, boolean deleteRecording, ITestResult result) {
        AutomationDriver driver = getDriver(result);
        VideoRecording.getInstance().stopRecording(driver, deleteRecording, testcase);
    }

    /**
     * Invoked each time a test is skipped.
     *
     * @param result ITestResult containing information about the run test.
     */

    public void onTestSkipped(ITestResult result) {
        BTLHelper.cleanupWebDrivers(result);
        if (result.getThrowable() != null) {
            String exceptionMessage = result.getThrowable().getMessage();
            if (exceptionMessage != null) {
                if (exceptionMessage.contains("UnreachableBrowserException") || exceptionMessage.contains("Connection refused")) {
                    String[] fixes = {
                            "If mobile/windows driver: start appium",
                            "If mobile/windows driver: verify that appium port is open",
                            "If mobile/windows driver: verify that a firewall isnt blocking appium",
                            "If desktop: verify that you have installed the browser on your computer",
                            "If desktop: verify that you have set driver binary to executable",
                            "If desktop: Verify that the path to the binary is correct",
                            "If iedriver: verify that configuration is correct, for more info visit " +
                                    "mdhttps://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver"};
                    Errors.logError(logger, "Failed to reach browser/app in webdriver test", fixes);
                } else if (exceptionMessage.contains("NullPointerException")) {
                    try {
                        AutomationDriver driver = getDriver(result);
                        if (driver.isAndroid()) {
                            if (driver.getAndroidDriver().currentActivity().equals("")) {
                                String[] fixes = {
                                        "If app crashed: remove the popup message and restart the test"};
                                Errors.logError(logger, "The app may have crashed and is unreachable", fixes);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Failed to retrieve webdriver instance");
                    }
                } else if (exceptionMessage.contains("xcodebuild failed with code 65")) {
                    String[] fixes = {
                            "Rebuild WebDriverAgent-Runner on the device manually from xCode"};
                    Errors.logError(logger, "Failed to sign xCode build, this happens occasionally likely due to a iOSDriver/xCode bug: https://github.com/appium/appium/issues/8918", fixes);
                } else if (exceptionMessage.contains("SessionNotCreatedException")) {
                    String[] fixes = {
                            "Enable session override for the device by setting the appium capability \"sessionOverride\" to true",
                            "If android: The device may be offline in the device list \"adb devices -l\" even though it's connected, disconnect and reconnect the device"};
                    Errors.logError(logger, "Could not start a new session, the cause may be due to an earlier session not closing as expected", fixes);
                } else if (exceptionMessage.contains("not in the list of connected devices")) {
                    String[] fixes = {
                            "Make sure the device is connected correctly",
                            "If android: The device may be offline in the device list \"adb devices -l\" even though it's connected, disconnect and reconnect the device"};
                    Errors.logError(logger, "Could not find the device", fixes);
                }
                logger.error("EXCEPTION THROWN: " + exceptionMessage);
            }
        }
        stopVideoRecording(result.getName(), false, result);
        logger.warn("TEST SKIPPED: " + result.getName() + "\n");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.trace("onTestFailedButWithinSuccessPercentage: " + result.getName() + "\n");
    }

}