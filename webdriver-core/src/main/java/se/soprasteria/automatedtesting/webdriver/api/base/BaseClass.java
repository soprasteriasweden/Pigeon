package se.soprasteria.automatedtesting.webdriver.api.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseclass.Execution;

/**
 *
 * Base function that provides base functionality that is used in all of the base functions.
 * The logger within this function will initialise with the class of the inheriting functions.
 *
 */
public abstract class BaseClass {
    /** Logger initialized in the constructor, outputs log with classname and timestamp */
    public Logger logger;

    protected BaseClass() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    /**
     * Suspends execution for specified time, a default warning will be thrown if interrupted
     *
     * @param millis time to sleep in milliseconds
     */
    public void sleep(int millis) {
        Execution.sleep(millis, logger, null);
    }

    /**
     * Suspends execution for specified time, you can specify a message
     *
     * @param millis time to sleep in milliseconds
     * @param message message to throw if sleep is interrupted
     */
    public void sleep(int millis, String message) {
        Execution.sleep(millis, logger, message);
    }
}
