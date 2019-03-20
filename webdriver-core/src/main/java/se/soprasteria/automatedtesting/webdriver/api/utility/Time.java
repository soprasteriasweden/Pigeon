package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.testng.ITestResult;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.time.TestClock;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.time.TimeUtil;

/**
 *
 * Utility functions to work with time strings
 *
 */
public class Time {

    /**
     * Returns the true offset in minutes between the two objects. For instance an offset between time1=0:10 and time2=0:00
     * would return 1430 (minutes in day 1440).
     *
     * @param time1
     * @param time2
     * @return The true offset in minutes between the two objects
     */
    public static long getMinutesOffset(String time1, String time2) {
        return TimeUtil.getMinutesDiff(time1, time2);
    }

    /**
     * Returns minutes between the two objects. For instance minutes between time1=0:10 and time2=0:00 would return
     * a value of -10
     *
     * @param time1
     * @param time2
     * @return Minutes between the two objects
     */
    public static long getMinutesBetween(String time1, String time2) {
        return TimeUtil.between(time1, time2);
    }

    /**
     * Returns the current time in Stockholm.
     *
     * @return The current time in Stockholm formatted as a String
     */
    public static String getStockholmTime() {
        return TimeUtil.getCurrentTime();
    }

    /**
     * Verifies that the string can be parsed into a LocalTime object.
     *
     * @param time
     * @return true i the string can be parsed into a LocalTime object, false if not.
     */
    public static boolean isValidTime(String time) {
        return TimeUtil.isValidTime(time);
    }

    /**
     * Verifies that the string can be parsed into a LocalDate object
     *
     * @param date
     * @return true if the string can be parsed into a LocalDate object, false if not.
     */
    public static boolean isValidDate(String date) {
        return TimeUtil.isValidDate(date);
    }

    /**
     * Gets the current time as a short string.
     *
     * @return - Time as string formatted hh:mm:ss, eg 17:25:10
     */
    public static String getCurrentTimeShort() {
        return TimeUtil.getCurrentTimeShort();
    }

    /**
     * Gets the current date as a short string.
     *
     * @return - Date as a string formatted yyyy/mm/dd, eg 2016/05/10
     */
    public static String getCurrentDateShort() {
        return  TimeUtil.getCurrentDateShort();
    }

    /**
     * Gets the start time of current test as a string suitable for file names.
     *
     * @param result - ITestResult object containing test result data
     * @return - Date and time as a string formatted yyyy-MM-dd--HH-mm-ss, eg 2018-05-10--12-53-38
     */
    public static String getCurrentTestStartTime(ITestResult result) {
        return TestClock.getCurrentTestStartTime(result);
    }

    /**
     * Gets the start time of test with the provided test ID as a string suitable for file names.
     *
     * @param testID - testID of test for which to get start time.
     * @return - Date and time as a string formatted yyyy-MM-dd--HH-mm-ss, eg 2018-05-10--12-53-38
     */
    public static String getTestStartTime(Long testID) {
        return TestClock.getTestStartTime(testID);
    }
}
