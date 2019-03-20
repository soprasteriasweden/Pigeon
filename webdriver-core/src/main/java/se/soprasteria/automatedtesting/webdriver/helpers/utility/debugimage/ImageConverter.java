package se.soprasteria.automatedtesting.webdriver.helpers.utility.debugimage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageConverter {

    public static BufferedImage convertPngToJpg(File pngFile) throws IOException {
        // Put png file in buffer
        BufferedImage bufferedImage = ImageIO.read(pngFile);
        // Create a blank buffered image with size taken from png buffered image, using RGB rather than ARGB
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        // write from png buffered image to new buffered image
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

        return newBufferedImage;
    }
}
