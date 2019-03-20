package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.Variables;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.errors.Console;

/**
 * Functions related to error handling/logging.
 */
public class Errors {

    /**
     * Standard error logging to the console.
     *
     * @param logger Logger used to by the function
     * @param message A message of the error that occured
     * @param possibleFix An array containing possible fixes to the error
     */
    public static void logError(Logger logger, String message, String[] possibleFix) {
        Console.logError(logger, message, possibleFix);
    }

    /**
     * A helper function that throws an illegal argument exception with specified message if
     * you pass null to it. Good to use when checking method arguments.
     *
     * @param object       Object to check for null
     * @param errorMessage Error message thrown in
     */
    public static void throwExceptionIfNull(Object object, String errorMessage) {
        Variables.throwExceptionOnNull(object, errorMessage);
    }

    /**
     * Returns an standardized error message based on input strings.
     *
     * @param message       A message of the error that occured
     * @param possibleFixes An array containing possible fixes to the error
     * @return String
     */
    public static String getErrorMessage(String message, String[] possibleFixes) {
        return Console.getErrorMessage(message, possibleFixes);
    }
}
