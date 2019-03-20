package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.JsonData;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.PatternMatcher;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.Streams;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.StringHelpers;

import java.util.List;
import java.util.Map;

/**
 * Utility functions related to managing data.
 */
public class Data {

    /**
     * Remove a list from another list using streams.
     *
     * @param from
     * @param what
     * @return
     */
    public static <T> List<T> listsExclude(List<T> from, List<T> what) {
        return Streams.exclude(from, what);
    }

    /**
     * Is the string a match to the supplied regular expression.
     *
     * @param string see if this string is a match.
     * @param regex
     * @return boolean with success of search.
     */
    public static boolean stringMatches(String string, String regex) {
        return PatternMatcher.matches(string, regex);
    }

    /**
     * Does the string contain supplied regular expression.
     *
     * @param string search this string for matches.
     * @param regex
     * @return boolean with success of search.
     */
    public static boolean stringContains(String string, String regex) {
        return PatternMatcher.contains(string, regex);
    }

    /**
     * Returns the groups found using supplied regular expression.
     *
     * @param string search this string for groups.
     * @param regex
     * @return List of strings containing all found groups.
     */
    public static List<String> stringGetGroups(String string, String regex) {
        return PatternMatcher.getGroups(string, regex);
    }

    /**
     * Does the string supplied contain a valid time.
     *
     * @param string
     * @return true if string contains valid time, false if not.
     */
    public static boolean stringContainsVerbalTime(String string) {
        return PatternMatcher.containsTime(string);
    }

    /**
     * Remove the regex part from the supplied string.
     *
     * @param string
     * @param regex
     * @return The resulted disected string.
     */
    public static String stringRemoveRegex(String string, String regex) {
        return PatternMatcher.remove(string, regex);
    }

    /**
     * Checks if a string is empty and if it is, overwrites its value with the override value.
     *
     * @param logger supplied for logging
     * @param check String to check if empty
     * @param override Value to set if the string was empty
     * @return The checked string
     */
    public static String ifEmptyOverride(Logger logger, String check, String override) {
        return StringHelpers.ifEmptyOverride(logger, check, override);
    }

    /**
     * Checks if a string is empty and if it is, overwrites it's value with the override value, otherwise convert string
     * to boolean. Defaults to false if not "true" is given.
     *
     * @param booleanCheck String containing boolean to check if empty
     * @param booleanOverride Value to set if the Boolean was empty
     * @return The checked Boolean
     */
    public static boolean ifEmptyOverrideBoolean(String booleanCheck, boolean booleanOverride) {
        return StringHelpers.ifEmptyOverrideBoolean(booleanCheck, booleanOverride);
    }

    /**
     * Checks if a string is empty and if it is, overwrites it's value with the override value, otherwise convert string
     * to double.
     *
     * @param doubleCheck String containing double to check if empty
     * @param doubleOverride Value to set if the double was empty
     * @return The checked double
     */
    public static double ifEmptyOverrideDouble(String doubleCheck, double doubleOverride) {
        return StringHelpers.ifEmptyOverrideDouble(doubleCheck, doubleOverride);
    }

    /**
     * Reads url with expectation it's a json file. Extracts value out of given key
     *
     * @param url pointing to a .json file
     * @param key wanting to extract value from
     * @return KeyValue as a Map
     */
    public static Map getJsonValueFromUrl(String url, String key) { return new JsonData().getValue(url, key); }

    /**
     * Reads url with expectation it's json file. Extracts values out of given keys
     *
     * @param url pointing to a .json file
     * @param keys wanting to extract values from
     * @return KeyValues as a Map
     */
    public static Map getJsonValuesFromUrl(String url, String[] keys) { return new JsonData().getValues(url, keys); }
}
