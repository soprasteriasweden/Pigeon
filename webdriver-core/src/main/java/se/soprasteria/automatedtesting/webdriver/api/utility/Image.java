package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.debugimage.ImageEditor;

import java.awt.image.BufferedImage;
import java.io.File;

public class Image {

    /**
     * Compare two images and returns the similarity percentage
     *
     * @param logger
     * @param image1PixelData - The control image pixel data.
     * @param image2PixelData - The image pixel data to compare with.
     * @param width - width of the image to compare
     * @param height - height of the imaget to compare
     * @return ImageComparisonData object containing the difference and similarity percentage of the overall comparison, the red, green and blue color of the pixel
     */
    public static ImageComparisonData compareImages(Logger logger,
                                      int[] image1PixelData,
                                      int[] image2PixelData,
                                      int width,
                                      int height) {
        return ImageEditor.compareImages(logger, image1PixelData, image2PixelData, width, height);
    }

    /**
     * Compare two images and returns the similarity percentage
     *
     * @param logger - The arguments to the script. May be empty.
     * @param image1 - The control image pixel data.
     * @param image2 - The image pixel data to compare with.
     * @return Similarity percentage
     */
    public static float compareImages(Logger logger,
                                                    File image1,
                                                    File image2) {
        return ImageEditor.compareImages(logger, image1, image2);
    }
}
