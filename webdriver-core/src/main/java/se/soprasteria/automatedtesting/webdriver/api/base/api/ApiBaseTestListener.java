package se.soprasteria.automatedtesting.webdriver.api.base.api;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;

/**
 * TestNG listener events. Used to automatically help with debugging and diagnosing failed tests. The listener is specified
 * in the testng XML with the 'listener' tag. This implements ITestListener.
 */
public class ApiBaseTestListener extends BaseClass implements ITestListener {

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

    /**
     * Captures screenshot and saves the pagesource when a test fails.
     *
     * @param result ITestResult containing information about the run test.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testmethod = result.getName();
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
        logger.info("TEST PASSED: " + testmethod + "\n");
    }


    /**
     * Invoked each time a test is skipped.
     *
     * @param result ITestResult containing information about the run test.
     */

    public void onTestSkipped(ITestResult result) {
        logger.warn("TEST SKIPPED: " + result.getName() + "\n");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.trace("onTestFailedButWithinSuccessPercentage: " + result.getName() + "\n");
    }

}
