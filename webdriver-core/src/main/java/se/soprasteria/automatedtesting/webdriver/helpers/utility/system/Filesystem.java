package se.soprasteria.automatedtesting.webdriver.helpers.utility.system;

import java.io.File;

/**
 *
 * Helper functions to perform check in the filesystem
 *
 */
public class Filesystem {

    /**
     *
     * Checks if path is valid
     *
     * @param path path to check
     * @return boolean
     */
    public static boolean isValidPath(String path) {
        try {
            File file = new File(path);
            if (!file.isDirectory())
                file = file.getParentFile();
            if (file.exists()){
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }
}
