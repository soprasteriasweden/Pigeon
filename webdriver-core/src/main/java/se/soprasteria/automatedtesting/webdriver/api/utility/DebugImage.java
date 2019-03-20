package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.debugimage.ImageEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A debugimage is a collection of images all merged into one big image. The idea is that you during the course of a
 * test can add images for debugpurposes. The images will be added one above eachother.
 */
public class DebugImage {

    /**
     * Append a new image into the debugimage. A new image will be appended below the previous one. You can specify a
     * text to write on the image as well as dimensions for a box to highlight in the image.
     *
     * @param logger
     * @param fileName - file name of the saved image
     * @param color - color to set on the graphics that is added on the image
     * @param image - Image to append.
     * @param text - Descriptive text to write into image.
     * @param x - x value from left for highlighted area.
     * @param y - y value from top for highlighted area.
     * @param width - Width of the highlighted box.
     * @param height - Width of the highlighted box.
     */
    public static void appendImage(Logger logger,
                                   String fileName,
                                   BufferedImage image,
                                   String text,
                                   Color color,
                                   int x,
                                   int y,
                                   int width,
                                   int height) {
        ImageEditor.appendImage(logger, fileName, image, text, color, x, y, width, height);
    }

    /**
     * Append a new image into the debugimage. A new image will be appended below the previous one. You can specify a
     * text to write on the image as well as dimensions for a box to highlight in the image.
     *
     * @param logger
     * @param image - Image to append.
     * @param text - Descriptive text to write into image.
     * @param fileName - file name of the saved image
     * @param color - color to set on the graphics that is added on the image
     */
    public static void appendImage(Logger logger,
                                   String fileName,
                                   BufferedImage image,
                                   String text,
                                   Color color) {
        ImageEditor.appendImage(logger, fileName, image, text, color);
    }

}
