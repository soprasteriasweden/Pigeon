package se.soprasteria.automatedtesting.webdriver.api.datastructures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;

/**
 * Class for managing debuglevel when running tests.
 */
public class DebugLevel extends BaseClass {
    /**
     * The key to the system property that holds the currently set debuglevel
     */
    public static final String DEBUG_LEVEL_KEY = "automatedtesting.debug.level";
    /**
     * The default level to set if no other level has been specified
     */
    public static final Level DEFAULT_LEVEL = Level.IMAGES_RESULT;

    /**
     * The levels available.
     * CONSOLE       - just log out to console, nothing more. Console logging is enabled in all other options as well.
     * DRIVERLOG     - Capture log from webdriver
     * IMAGES_RESULT - Capture a screenshot of what was on screen when the test ended. Occurs on both pass and skip.
     * IMAGES        - Capture screenshots on all events, this includes edits to page, clicks, types, opening pages, test pass/fail.
     * IMAGES_CLICKS - Capture screenshots on click and higher level events, this includes clicks, open pages, test pass/fail
     * IMAGES_PAGES  - Capture screenshots on pageloads and test pass/fails
     */
    public enum Level {
        CONSOLE,
        DRIVERLOG,
        IMAGES,
        IMAGES_CLICKS,
        IMAGES_PAGES,
        IMAGES_RESULT,
        IMAGES_FAIL,
        DRIVERLOG_AND_IMAGES,
        DRIVERLOG_AND_IMAGES_FAIL,
    }

    /**
     * Get the current debuglevel set in the system property.
     *
     * @return current debuglevel
     */
    public static Level getLevel() {
        String level = System.getProperty(DEBUG_LEVEL_KEY);
        return Level.valueOf(getValidLevelName(level));
    }

    /**
     * Set the system property for which debuglevel to use.
     *
     * @param level to set debugging to as a Level.
     */
    public static void set(Level level) {
        System.setProperty(DEBUG_LEVEL_KEY, level.name());
    }

    /**
     * Set the system property for which debuglevel to use.
     *
     * @param level as a String. Needs to be exactly the name of the enum to set, eg IMAGES_CLICKS. If an
     *              invalid value is passed this set function will use the current value. If there is no current
     *              value the default value will be used.
     */
    public static void set(String level) {
        level = getValidLevelName(level);
        getLogger().info("Setting debugLevel to " + level);
        System.setProperty(DEBUG_LEVEL_KEY, level);
    }

    private static String getValidLevelName(String level) {
        Logger logger = getLogger();
        try {
            Level.valueOf(level);
        } catch (Exception e) {
            if (level == null) {
                logger.warn("Found no debuglevel(null), returning default level");
            } else {
                logger.warn("Found illegal debuglevel(" + level + "), returning default level");
            }
            String validValues = "";
            for (Level l : Level.values()) {
                validValues += l.name() + ", ";
            }
            logger.warn("Valid values are: " + validValues);
            level = DEFAULT_LEVEL.name();
        }
        return level;
    }

    private static Logger getLogger() {
        return LogManager.getLogger(DebugLevel.class.getName());
    }
}
