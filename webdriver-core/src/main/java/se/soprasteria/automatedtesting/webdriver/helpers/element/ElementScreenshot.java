package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.SkipException;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.Screenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.image.PixelGrabber;

public class ElementScreenshot extends BaseClass {

    /**
     * Capture screenshot of element
     */

    private final AutomationDriver driver;
    private final int SCREENSHOT_ELEMENT_MARGIN = 1;

    public ElementScreenshot(AutomationDriver driver) {
        this.driver = driver;
    }

    public File captureElementScreenshot(WebElement element) {
        if(driver.isMobile() && !driver.getContext().toLowerCase().contains("native")) {
            throw new SkipException("Capturing element screenshots is not supported on web context apps or mobile browsers.");
        }
        logger.info("When capturing screenshots of elements the browser zoom level and windows/mac screen scaling must be at 100%");
        try {
            File elementScreenshot = Screenshot.captureScreenshotAsFile();
            BufferedImage bufferedImage = ImageIO.read(elementScreenshot);
            Point point = getAdjustedPointForScreenshot(element);
            Dimension elementDimension = getAdjustedSizeForScreenshot(element);
            BufferedImage elementImage = bufferedImage.getSubimage(point.getX(), point.getY(), elementDimension.getWidth(), elementDimension.getHeight());
            ImageIO.write(elementImage, "png", elementScreenshot);
            return elementScreenshot;
        } catch (Exception e) {
            throw new RuntimeException("Could not generate a screenshot of element: " + e.getMessage());
        }
    }

    public int[] getImagePixels(BufferedImage image){
        int[] pixels;
        PixelGrabber imagePixels;
        pixels = new int[image.getWidth() * image.getHeight()];
        imagePixels = new PixelGrabber(image,0,0,image.getWidth(),image.getHeight(),pixels,0,image.getWidth());
        try {
            imagePixels.grabPixels();
        } catch
        (InterruptedException e){
            logger.info(e.getMessage());
            throw new RuntimeException("Could not grab the pixels from the image" + e.getMessage());
        }
        return pixels;
    }


    private Dimension getAdjustedSizeForScreenshot(WebElement webElement){
        int adjustedWidth = webElement.getSize().getWidth() - SCREENSHOT_ELEMENT_MARGIN;
        int adjustedHeight = webElement.getSize().getHeight() - SCREENSHOT_ELEMENT_MARGIN;
       return new Dimension(adjustedWidth,adjustedHeight);
    }

    private Point getAdjustedPointForScreenshot(WebElement webElement){
        int scrollYOffset = ((Long)driver.executeJavaScript("return window.pageYOffset;")).intValue();
        int scrollXOffset = ((Long)driver.executeJavaScript("return window.pageXOffset;")).intValue();
        int xValue = webElement.getLocation().getX() + SCREENSHOT_ELEMENT_MARGIN - scrollXOffset;// - scrollXOffset;
        int yValue = webElement.getLocation().getY() + SCREENSHOT_ELEMENT_MARGIN - scrollYOffset;// - scrollYOffset;
        return new Point(xValue,yValue);
    }
}
