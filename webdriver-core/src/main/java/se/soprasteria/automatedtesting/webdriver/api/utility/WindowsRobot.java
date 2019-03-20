package se.soprasteria.automatedtesting.webdriver.api.utility;

import se.soprasteria.automatedtesting.webdriver.helpers.utility.windowsrobot.WindowsJNA;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.windowsrobot.WindowsJavaRobot;

import java.awt.*;

/**
 * A robot that can manipulate processes and navigate the Windows OS. Uses a combination of
 * Java Robot and JNA based on the action.
 *
 */
public class WindowsRobot {
    private WindowsJNA window;

    /**
     * Initialize a new window to work with. The window needs to be open in the OS when initializing.
     *
     * @param title Title of the window to manage.
     * @param className If a java application, provide classname, if unknown provide null.
     */
    public WindowsRobot(String title, String className) {
        this.window = WindowsJNA.findWindow(className, title);
    }

    /**
     * Resizes the window to the following specifications.
     *
     * @param x x-value for top-left corner
     * @param y y-value for top-left corner
     * @param width window width in pixels
     * @param height window height in pixels
     */
    public void moveAndResize(int x, int y, int width, int height) {
        window.moveAndResize(new Rectangle(x,y,width,height));
    }

    /**
     * Opens the application in windows using the winkey+s command.
     *
     * @param title The name of the app to open. This should be the textstring that shows the wanted app as the first
     *              result in the windows search found in the startmenu.
     */
    public static void openApplication(String title) {
        WindowsJavaRobot.startApplication(title);
    }

    /**
     * Exits the currently opened application using the alt+f4 command.
     */
    public static void exitCurrentApplication() {
        WindowsJavaRobot.exitApplication();
    }
}
