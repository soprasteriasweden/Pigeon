package se.soprasteria.automatedtesting.webdriver.helpers.utility.windowsrobot;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;

import java.awt.*;
import java.awt.event.KeyEvent;


public class WindowsJavaRobot extends BaseClass {
    private static final int[] F_KEYS = {-1, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4};

    public static void minimizeAllWindows() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            // TODO
        }
    }

    public static void startApplication(String applicationName) {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            type(robot, applicationName);
            pressEnter(robot);
            robot.delay(5000);
        } catch (AWTException e) {
            System.out.println("Failed to start application with search string: " + applicationName);
        }
    }

    public static void toggleApplicationFullscreen() {
        try {
            Robot robot = new Robot();
            robot.delay(200);
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(200);
        } catch (AWTException e) {
            System.out.println("Failed to toggle application to fullscreen using alt+enter shortkey");
        }
    }

    public static void dockApplicationToLeft() {
        dockApplicationToSide(KeyEvent.VK_LEFT);
    }

    public static void exitApplication() {
        try {
            Robot robot = new Robot();
            robot.delay(200);
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_F4);
            robot.delay(200);
        } catch (AWTException e) {
            System.out.println("Failed to exit application using shortkey alt+f4");
        }
    }

    public static void type(Robot robot, String s) {
        for (int i=0; i<s.length(); i++) {
            pressKey(robot, s.charAt(i));
        }
    }

    public static void pressEnter(Robot robot) {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    private static void dockApplicationToSide(int side) {
        try {
            Robot robot = new Robot();
            robot.delay(200);
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(side);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
            robot.keyRelease(side);
            robot.delay(200);
        } catch (AWTException e) {
            System.out.println("Failed to dock application to the side");
        }
    }

    private static int getKeyCode(char key) {
        int code = String.valueOf(key).getBytes()[0];
        // keycode only handles [A-Z] (which is ASCII decimal [65-90])
        if (code > 96 && code < 123) {
            code = code - 32;
        }
        return code;
    }

    private static void pressKey(Robot robot, char key) {
        robot.delay(40);
        robot.keyPress(getKeyCode(key));
        robot.keyRelease(getKeyCode(key));
    }

}
