package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;

import java.text.DecimalFormat;


public class StringHelpers {

    private final static DecimalFormat decimalFormat = new DecimalFormat("#.#####");

    public static String ifEmptyOverride(Logger logger, String check, String override) {
        if (check == null) {
            Errors.throwExceptionIfNull(override, "Tried to call ifEmptyOverride with two null parameters, probable bug");
            check = override;
        }
        if (!check.contentEquals("")) {
            return check;
        } else if (override.contentEquals("")) {
            logger.warn("Ran ifEmptyOverride with an empty value, probably bug, no exception thrown");
        }
        return override;
    }

    public static Boolean ifEmptyOverrideBoolean(String check, boolean override) {
        Errors.throwExceptionIfNull(check, "Tried to call ifEmptyOverrideBoolean with null parameter, probable bug");
        if(check.isEmpty()) return override;
        else return Boolean.valueOf(check);
    }

    public static double ifEmptyOverrideDouble(String check, double override) {
        Errors.throwExceptionIfNull(check, "Tried to call ifEmptyOverrideDouble with null parameter, probable bug");
        if(check.isEmpty()) return override;
        else return Double.parseDouble(check);
    }

    public static DecimalFormat GetDecimalFormat(){
        return decimalFormat;
    }
}
