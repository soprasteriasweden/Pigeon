package se.soprasteria.automatedtesting.webdriver.helpers.base.baseclass;

import org.apache.logging.log4j.Logger;


public class Execution {

    public static void sleep(int millis, Logger logger, String message) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            if (message == null) {
                logger.warn(message);
            } else {
                logger.warn("Sleep was interrupted");
            }
        }
    }

}
