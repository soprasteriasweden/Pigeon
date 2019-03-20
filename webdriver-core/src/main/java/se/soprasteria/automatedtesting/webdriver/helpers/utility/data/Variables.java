package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

/**
 * Helper functions to verify different values for variables.
 *
 */
public class Variables {

    public static void throwExceptionOnNull(Object object, String errorMessage) {
        if (object == null) {
            if (errorMessage == null) {
                errorMessage = "missing";
            }
            throw new RuntimeException("Object failed null check, Message: " + errorMessage);
        }
    }

}
