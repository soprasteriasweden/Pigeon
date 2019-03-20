package se.soprasteria.automatedtesting.webdriver.helpers.utility.errors;

import org.apache.logging.log4j.Logger;

/**
 *
 * Logging functions specific for console logging.
 *
 */
public class Console {

    public static void logError(Logger logger, String message, String[] possibleFix) {
        logger.error("ERROR: " + message);
        for (String fix: possibleFix) {
            logger.error("POSSIBLE FIX: " + fix);
        }
    }

    public static String getErrorMessage(String message, String[] possibleFix) {
        String errorString = ("ERROR: " + message + "   ");
        for (String fix: possibleFix) {
            errorString += " POSSIBLE FIX: " + fix + ". ";
        }
        return errorString;
    }

}
