package se.soprasteria.automatedtesting.webdriver.helpers.utility;

import com.google.common.base.Stopwatch;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Direction;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseclass.Execution;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class AppiumHelper {

    private static Platform platform;
    private static int appiumPort = -1;
    private static String appiumFilter;
    private static final int timeoutMillis = 20000;

    public static void initializeVariables(int port) {
        if(appiumPort == -1) {
            appiumPort = port;
            if(Platform.getCurrent().is(Platform.WINDOWS)) {
                platform = Platform.WINDOWS;
                String systemPath = System.getenv("WINDIR");
                appiumFilter = "\"WINDOWTITLE eq " + systemPath.substring(0,4).toUpperCase() + systemPath.substring(4).toLowerCase() +
                        "\\system32\\cmd.exe - appium*\"";
            } else if(Platform.getCurrent().is(Platform.MAC)) {
                platform = Platform.MAC;
                appiumFilter = "appium --port " + appiumPort;
            } else if(Platform.getCurrent().is(Platform.LINUX)) {
                platform = Platform.LINUX;
                appiumFilter = "appium --port " + appiumPort;
            } else {
                throw new RuntimeException("The framework does not currently support Appium automation for your OS");
            }
        }
    }

    public static void startAppium(Logger logger, String port) {
        initializeVariables(Integer.parseInt(port));
        Process appium = null;
        String appiumArguments = " " + BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_ARGUMENTS) + " ";
        if(appiumArguments.isEmpty()) appiumArguments = " ";
        if(appiumArguments.contains("--port")) {
            throw new IllegalArgumentException("Do not include appium argument --port, this argument is automatically " +
                    "set by the framework based on the configuration ID");
        }

        if(Boolean.valueOf(BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_LOG))) {
            if(appiumArguments.contains("--log")) {
                throw new IllegalArgumentException("Do not include appium argument --log if the ConfigurationOption " +
                        "'config.appium.log' is set to true, this argument is automatically set by the framework");
            }
            appiumArguments = appiumArguments.concat("--log " + System.getProperty("user.dir") + "/appium_log.log");
        }

        if(!isAppiumRunning(logger)) {
            String[] appiumStartScript = new String[] {};
            String loggerMessage = "";
            if(platform == Platform.WINDOWS) {
                appiumStartScript = new String[] { "cmd", "/c", "start", "cmd.exe", "/K", "appium --port " + appiumPort + appiumArguments };
                loggerMessage = "Command Prompt and Appium could not be started correctly: ";
            } else if(platform == Platform.MAC) {
                appiumStartScript = new String[] { "osascript", "-e", "tell app \"Terminal\"\ndo script \"appium --port " + appiumPort + appiumArguments + "\"\nend tell" };
                loggerMessage = "Terminal and Appium could not be started correctly: ";
            } else if(platform == Platform.LINUX) {
                appiumStartScript = new String[] { "/bin/sh", "-c", "gnome-terminal --title='appium --port " + appiumPort + "'   -- appium --port " + appiumPort + appiumArguments };
                loggerMessage = "Terminal and Appium could not be started correctly: ";
            }

            ProcessBuilder builder = new ProcessBuilder(appiumStartScript);
            builder.redirectErrorStream(true);
            try {
                appium = builder.start();
            } catch (Exception e) {
                logger.trace(loggerMessage + e.getMessage());
            }
        }

        Stopwatch timeout = Stopwatch.createStarted();
        while(timeout.elapsed(TimeUnit.MILLISECONDS) < timeoutMillis) {
            if(isAppiumRunning(logger)) break;
        }
        if(timeout.elapsed(TimeUnit.MILLISECONDS) > timeoutMillis) {
            throw new RuntimeException("Could not start Appium within the timeout limit of " + timeoutMillis + " millis");
        }
        timeout.stop();
        if(appium !=null) appium.destroyForcibly();

    }

    public static void stopAppium(Logger logger){
        try {
            if(platform == Platform.WINDOWS) {
                Stopwatch timeout = Stopwatch.createStarted();
                while(isAppiumRunning(logger) && timeout.elapsed(TimeUnit.MILLISECONDS) < timeoutMillis) {
                    Runtime.getRuntime().exec("TASKKILL /F /FI " + appiumFilter);
                    Execution.sleep(100, logger, null);
                }
                if(timeout.elapsed(TimeUnit.MILLISECONDS) > timeoutMillis) logger.trace("Could not stop Appium within the timeout limit of " + timeoutMillis + " millis");
                timeout.stop();
            } else if(platform == Platform.MAC) {
                if(isAppiumRunning(logger)) {
                    ProcessBuilder builder = new ProcessBuilder("osascript", "-e", "tell app \"Terminal\"\n" +
                            "activate (every window whose name contain \"" + appiumFilter + "\")\n" +
                            "close (first window whose name contain \"" + appiumFilter + "\")\n" +
                            "tell application \"System Events\" to key code 36\n" +
                            "end tell", "& exit");
                    builder.redirectErrorStream(true);
                    builder.start();
                }
            } else if(platform == Platform.LINUX) {
                if(isAppiumRunning(logger)) {
                    ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "kill `ps -ef | grep \"appium --port " + appiumPort + "\" | grep -v grep`");
                    builder.redirectErrorStream(true);
                    builder.start();
                }
                Execution.sleep(4000, logger, null);
            }
        } catch (IOException e) {
            logger.trace("Could not close Appium correctly: " + e.getMessage());
        }

    }

    private static boolean isAppiumRunning(Logger logger) {
        try (Socket ignored = new Socket("localhost", appiumPort)) {
            return true;
        } catch (IOException expected) {
            return false;
        }
    }

    private static boolean isAppiumProcessRunning(Logger logger) {
        boolean running = false;
        BufferedReader stdInput;
        if(platform == Platform.WINDOWS) {
            try {
                stdInput = new BufferedReader(new InputStreamReader(
                        Runtime.getRuntime().exec("TASKLIST /FI " + appiumFilter).getInputStream()));
                String line;
                while((line = stdInput.readLine()) != null) {
                    if(line.toLowerCase().contains("image name")) {
                        running = true;
                        break;
                    }
                }
            } catch (IOException e) {
                logger.trace("Could not perform command to check Appium process status: " + e.getMessage());
            }
        } else if(platform == Platform.MAC) {
            try {
                ProcessBuilder builder = new ProcessBuilder("ps", "-ax");
                builder.redirectErrorStream(true);
                Process p = builder.start();
                stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while((line = stdInput.readLine()) != null) {
                    if(line.toLowerCase().contains(appiumFilter)) {
                        running = true;
                        break;
                    }
                }
                p.destroyForcibly();
            } catch (IOException e) {
                logger.trace("Could not perform command to check Appium process status: " + e.getMessage());
            }
        } else if(platform == Platform.LINUX) {
            try {
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ps -ef | grep \"appium --port " + appiumPort + "\" | grep -v grep");
                builder.redirectErrorStream(true);
                Process p = builder.start();
                stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while((line = stdInput.readLine()) != null) {
                    if(line.toLowerCase().contains(appiumFilter)) {
                        running = true;
                        break;
                    }
                }
                p.destroyForcibly();
            } catch (IOException e) {
                logger.trace("Could not perform command to check Appium process status: " + e.getMessage());
            }
        } else {
            logger.trace("Your is OS currently not supported to check for Appium process status, returning default value false");
        }
        return running;
    }

    public static void swipe(AutomationDriver driver, Logger logger, Direction direction, int swipeLocation, int swipeLength, Speed speed) {
        // TODO fill in this function, it needs to be implemented.

        if(driver.isMobile() && swipeLocation > 0 && swipeLocation < 100 && swipeLength > 0 && swipeLength < 100 ) {

            AppiumDriver appiumDriver = driver.getAppiumDriver();

            String originalContext = appiumDriver.getContext();
            appiumDriver.context("NATIVE_APP");

            WaitOptions duration = new WaitOptions().withDuration(getSwipeDuration(speed));

            TouchAction touchAction = new TouchAction(appiumDriver);

            int width = appiumDriver.manage().window().getSize().width;
            int height = appiumDriver.manage().window().getSize().height;

            double swipeLocationPercentage = swipeLocation / 100.0;
            double offset = ((100 - swipeLength) / 100.0) / 2;

            int top = (int)(height * ((0 + offset) + (swipeLength / 100.0)));
            int bottom = (int) (height * ((1 - offset) - (swipeLength / 100.0)));
            int left = (int) (width * ((1 - offset) - (swipeLength / 100.0)));
            int right = (int) (width * ((0 + offset) + (swipeLength / 100.0)));

            PointOption swipeStart = new PointOption();
            PointOption swipeEnd = new PointOption();

            if (direction == Direction.LEFT) {
                logger.debug("Swiping left with a distance of " + swipeLength + "% of the horizontal screen size starting at Y = 0 + " + swipeLocation + "% of the vertical screen size");
                swipeStart.withCoordinates(right, (int)(height * swipeLocationPercentage));
                swipeEnd.withCoordinates(left, (int)(height * swipeLocationPercentage));
                touchAction.press(swipeStart).waitAction(duration).moveTo(swipeEnd).release().perform();
            } else if (direction == Direction.RIGHT) {
                logger.debug("Swiping right with a distance of " + swipeLength + "% of the horizontal screen size starting at Y = 0 + " + swipeLocation + "% of the vertical screen size");
                swipeStart.withCoordinates(left, (int)(height * swipeLocationPercentage));
                swipeEnd.withCoordinates(right, (int)(height * swipeLocationPercentage));
                touchAction.press(swipeStart).waitAction(duration).moveTo(swipeEnd).release().perform();
            } else if (direction == Direction.UP) {
                logger.debug("Swiping up with a distance of " + swipeLength + "% of the vertical screen size starting at X = 0 + " + swipeLocation + "% of the horizontal screen size");
                swipeStart.withCoordinates((int)(width * swipeLocationPercentage), top);
                swipeEnd.withCoordinates((int)(width * swipeLocationPercentage), bottom);
                touchAction.press(swipeStart).waitAction(duration).moveTo(swipeEnd).release().perform();
            } else if (direction == Direction.DOWN) {
                logger.debug("Swiping down with a distance of " + swipeLength + "% of the vertical screen size starting at X = 0 + " + swipeLocation + "% of the horizontal screen size");
                swipeStart.withCoordinates((int)(width * swipeLocationPercentage), bottom);
                swipeEnd.withCoordinates((int)(width * swipeLocationPercentage), top);
                touchAction.press(swipeStart).waitAction(duration).moveTo(swipeEnd).release().perform();
            }
            appiumDriver.context(originalContext);
        } else if(!driver.isMobile()){
            logger.error("Android or iOS driver is needed to perform a swipe");
        } else {
            logger.error("Swipe location and swipe length needs to be an int between 0 and 100 representing the location on the screen in percentage, relative to screen size");
        }

    }

    private static Duration getSwipeDuration(Speed speed) {
        switch (speed) {
            case SLOW:
                return Duration.ofMillis (3000);
            case MEDIUM:
                return Duration.ofMillis (2000);
            case FAST:
                return Duration.ofMillis (1000);
            case VERY_FAST:
                return Duration.ofMillis (300);
            default:
                return Duration.ofMillis (1000);
        }
    }

}
