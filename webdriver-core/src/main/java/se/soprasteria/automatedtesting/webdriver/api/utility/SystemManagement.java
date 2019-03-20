package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.system.Filesystem;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.system.Operatingsystem;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.system.PropertyHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.system.Resources;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * Utility functions related to system management.
 *
 */
public class SystemManagement {

    /**
     * Checks if the given path is valid.
     *
     * @param path
     * @return boolean
     */
    public static boolean isValidPath(String path) {
        return Filesystem.isValidPath(path);
    }

    /**
     * Checks if we are running on a Windows OS currently.
     *
     * @return boolean
     */
    public static boolean isWindows() {
        return Operatingsystem.isWindows();
    }

    /**
     * Provides an absolute path to a resource runtime, basically translates a classpath into an absolute path.
     * Note: for this to work the resource you are trying to access needs to be either in the resource folder
     * or the class path.
     *
     * @param resourcePath relative path to the Java resource without the leading forward slash, eg 'folder/file.extension' .
     * @return String absolute path to resource in os.
     */
    public static String resourceGetAbsolutePath(String resourcePath) {
        return Resources.getAbsolutePath(resourcePath);
    }

    /**
     * Returns the a runtime resource located in the classpath as a stream.
     *
     * @param resourcePath relative path to the Java resource.
     * @return The runtime resource located in the classpath as a stream.
     */
    public static InputStream resourceGetAsStream(String resourcePath) {
        return Resources.getAsStream(resourcePath);
    }

    /**
     * Get properties from the classpath.
     *
     * @param logger Logger supplied for log output
     * @param file Relative filename for the classpath
     * @return Properties object
     * @throws RuntimeException if file not found or invalid
     */
    public static Properties getPropertiesFromClassPath(Logger logger, String file) {
        return PropertyHelper.getProperties(logger, file);
    }


    /**
     * Extract a resource from the jar into a temporary directory that will be cleaned out once the program has exited.
     *
     * @param resource the relative path to the resource you wish to access from the filesystem
     * @param name the name of the temporary file in the temporary directory
     * @return the path to the file in the temporary directory
     */
    public static String moveResourceToTemporaryDirectory(String resource, String name) {
        return Resources.moveResourceToTemp(resource, name);
    }


    /**
     * Iterates through the supplied properties and overrides a property if there exists a system property with the same
     * name. Basically used to override properties set in file with properties specified in commandline/java config.
     *
     * @param logger Logger supplied for log output
     * @param properties overriding properties
     */
    public static void overrideSystemProperties(Logger logger, Properties properties) {
        PropertyHelper.overrideSystemProperties(logger, properties);
    }
}
