package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import se.soprasteria.automatedtesting.webdriver.api.utility.Image;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.StringHelpers;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.SessionCapabilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * Class to capture screenshot. Capture screenshot is automatically called by the testlistener when a test fails.
 *
 */
public class Screenshot {

    private static AutomationDriver driver;
    private static Logger logger;


    public static void initializeScreenshot(AutomationDriver driver, Logger logger) {
        Screenshot.driver = driver;
        Screenshot.logger = logger;
    }

    public static void captureScreenshot(File outputFile) throws IOException {
        if (!canTakeScreenshots(driver)) {
            return;
        }
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, outputFile);
    }

    public static BufferedImage captureScreenshot() throws IOException {
        if (!canTakeScreenshots(driver)) {
            return getUnsupportedImage(driver);
        }
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean nativeScreenshotEnabled = false;
        try {
            nativeScreenshotEnabled = Boolean.valueOf(SessionCapabilities.getCapability("nativeWebScreenshot"));
        } catch (IllegalArgumentException expected) { }
        if (driver.isAndroid() && !nativeScreenshotEnabled) logger.warn("Android detected. If taking screenshot gets stuck, pass capability: \"nativeWebScreenshot\" = \"true\" to webdriver config");
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(screenshot);
        LogManager.getLogger(Screenshot.class).debug("Took screenshot, elapsed time: " + stopwatch);
        stopwatch.reset();
        return image;
    }

    public static File captureScreenshotAsFile() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean nativeScreenshotEnabled = false;
        try {
            nativeScreenshotEnabled = Boolean.valueOf(SessionCapabilities.getCapability("nativeWebScreenshot"));
        } catch (IllegalArgumentException expected) { }
        if (driver.isAndroid() && !nativeScreenshotEnabled) logger.warn("Android detected. If taking screenshot gets stuck, pass capability: \"nativeWebScreenshot\" = \"true\" to webdriver config");
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        LogManager.getLogger(Screenshot.class).debug("Took screenshot, elapsed time: " + stopwatch);
        stopwatch.reset();
        return screenshot;
    }

    private static boolean canTakeScreenshots(AutomationDriver driver) {
        if (driver.isHtmlUnitDriver()) {
            logger.warn("Tried to take screenshot with unsupported , type: " + driver.getWebDriverName());
            return false;
        }

        return true;
    }


    private static BufferedImage getUnsupportedImage(AutomationDriver driver) {
        BufferedImage img = new BufferedImage(
                1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.dispose();
        return img;
    }

    public static boolean didScreenChangeDuringInterval(int timeInterval) {
        float result = compareScreen(timeInterval);
        if(result != 100) {
            logger.trace("The screen changed during the interval with a difference of " + StringHelpers.GetDecimalFormat().format(100f - result) + "%");
            return true;
        } else {
            logger.trace("The screen was identical after the interval");
            return false;
        }
    }

    public static boolean didScreenChangeDuringInterval(float similarityPercentageThreshold, int timeInterval) {
        float result = compareScreen(timeInterval);
        if(result < similarityPercentageThreshold) {
            logger.trace("The screen changed during the interval and had a similarity percentage below the threshold of "+ similarityPercentageThreshold + "%");
            return true;
        } else {
            logger.trace("The screen had a similarity percentage above the threshold of " + similarityPercentageThreshold + "% after the interval");
            return false;
        }
    }

    private static float compareScreen(int timeoutMillis) {
        try {
            File imageBeforeInterval = captureScreenshotAsFile();
            Thread.sleep(timeoutMillis);
            File imageAfterInterval = captureScreenshotAsFile();
            float screenshotSimilarity = Image.compareImages(logger, imageBeforeInterval, imageAfterInterval);
            logger.trace("The screen had a visual similarity of " + StringHelpers.GetDecimalFormat().format(screenshotSimilarity) + "%");
            return screenshotSimilarity;
        } catch (Exception e) {
            throw new RuntimeException("Could not capture a screenshot: " + e.getMessage());
        }

    }


}
