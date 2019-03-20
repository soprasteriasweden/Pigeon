package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.api.utility.Image;
import se.soprasteria.automatedtesting.webdriver.api.utility.ImageComparisonData;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.StringHelpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Comparison extends BaseClass {
    /**
     * Compares element/s in the class specified in constructor
     */

    private final AutomationDriver driver;
    private ElementScreenshot elementScreenshot;

    public Comparison(AutomationDriver driver) {
        this.driver = driver;
        this.elementScreenshot = new ElementScreenshot(driver);
    }

    public boolean didElementChangeDuringInterval(WebElement element, int timeoutMillis){
       ImageComparisonData comparisonData = compareElements(element,timeoutMillis);
        if (comparisonData.getSimilarityPercentage() != 100){
            logger.trace("The element changed during the interval with a difference of " + getDecimalFormat(comparisonData.getDifferencePercentage()) + "%");
            return true;
        } else {
            logger.trace("The element was identical after the interval");
            return false;
        }
    }

    public boolean didElementChangeDuringInterval(WebElement element, float similarityPercentageThreshold, int timeoutMillis){
        ImageComparisonData comparisonData = compareElements(element,timeoutMillis);
        if (comparisonData.getSimilarityPercentage() < similarityPercentageThreshold){
            logger.trace("The element changed during the interval and had a similarity percentage below the threshold of "+ similarityPercentageThreshold + "%");
            return true;
        } else {
            logger.trace("The element had a similarity percentage above the threshold of " + similarityPercentageThreshold + "% after the interval");
            return false;
        }
    }

    private ImageComparisonData compareElements(WebElement element, int timeoutMillis) {
        int[] image1Data;
        int[] image2Data;
        ImageComparisonData comparisonData;
        try {
            File fileImage1 = elementScreenshot.captureElementScreenshot(element);
            logger.trace("Took a screenshot of the element before the interval");
            BufferedImage image1 = ImageIO.read(fileImage1);
            image1Data = elementScreenshot.getImagePixels(image1);
            logger.trace("Grabbed the pixels of the screenshot before the interval");
            sleep(timeoutMillis);
            File fileImage2 = elementScreenshot.captureElementScreenshot(element);
            logger.trace("Took a screenshot of the element after the interval");
            BufferedImage image2 = ImageIO.read(fileImage2);
            image2Data = elementScreenshot.getImagePixels(image2);
            logger.trace("Grabbed the pixels of the screenshot after the interval");

            if (image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight()) {
                    comparisonData = Image.compareImages(logger, image1Data, image2Data, image2.getWidth(), image1.getHeight());
                    logger.trace("The element had a visual similarity of " + getDecimalFormat(comparisonData.getSimilarityPercentage()) + "%");
            } else {
                throw new SkipException("The two images does not have the same size(IMAGE BEFORE INTERVAL: W: " + image1.getWidth() + " H: " + image1.getHeight() + " IMAGE AFTER THE INTERVAL: " + " W: " + image2.getWidth() + "H: " + image2.getHeight() + ") and could not be compared, skipping test. " +
                        "(There is currently an issue comparing images with different scroll bar existence on Firefox and Safari, respective driver crops out any potential scroll bar resulting in " +
                        "different image resolutions and non comparable images)");
            }
         return comparisonData;
        } catch (Exception e) {
            throw new RuntimeException("Could not capture a screenshot of the given element: " + e.getMessage());
        }
    }

    public String getDecimalFormat(double value){
        return StringHelpers.GetDecimalFormat().format(value);
    }
}
