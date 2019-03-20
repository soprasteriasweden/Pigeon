package se.soprasteria.automatedtesting.webdriver.helpers.utility.system;


public class Operatingsystem {

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

}
