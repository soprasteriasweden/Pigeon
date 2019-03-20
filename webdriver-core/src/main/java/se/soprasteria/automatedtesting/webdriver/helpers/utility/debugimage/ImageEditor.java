package se.soprasteria.automatedtesting.webdriver.helpers.utility.debugimage;

import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.Logger;
import org.testng.SkipException;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.api.utility.ImageComparisonData;
import se.soprasteria.automatedtesting.webdriver.api.utility.Time;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class ImageEditor {

    private static final String SUREFIRE_FOLDER = "target/surefire-reports";

    private static String FILE_FORMAT = "jpg";
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList("jpg", "png", "gif", "bmp");

    private static double FACTOR = 1.0d;
    private static final double MINIMUM_FACTOR = 0.1d;
    private static final double MAXIMUM_FACTOR = 1.0d;

    public static void appendImage(Logger logger, String identifier, BufferedImage image, String text, Color color,
                                   int x, int y, int width, int height) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.drawLine(x, y, x + width, y);
        g.drawLine(x + width, y, x + width, y + height);
        g.drawLine(x, y + height, x + width, y + height);
        g.drawLine(x, y, x, y + height);
        g.dispose();
        appendImage(logger, identifier, image, text, color);
    }

    public static void appendImage(Logger logger, String identifier, BufferedImage image, String text, Color color) {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        if (!new File(SUREFIRE_FOLDER).exists()) {
            File surefireFolder = new File(SUREFIRE_FOLDER);
            logger.warn("Failed to save images due to missing target directory, expected location: " +
                    surefireFolder.getAbsolutePath());
            logger.warn("Creating folder " + surefireFolder.getAbsolutePath());
            surefireFolder.mkdir();
        }

        String filename = SUREFIRE_FOLDER + File.separator + identifier + "." + FILE_FORMAT;

        if (new File(filename).exists()) {
            text = " " + Time.getCurrentTimeShort() + " - " + text + " ";
        } else {
            text = Time.getCurrentDateShort() + " - " + Time.getCurrentTimeShort() + " - " + text + " ";
        }

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(30f));
        int x = 30;
        int y = 30;

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setColor(Color.black);
        g.fillRect(x,
                y - fm.getAscent(),
                (int) rect.getWidth(),
                (int) rect.getHeight());

        g.setColor(color);

        g.drawString(text, x, y);
        g.dispose();
        try {
            if (new File(filename).exists()) {
                BufferedImage compilation = ImageIO.read(new File(filename));
                compilation = joinBufferedImageVertical(compilation, getScaledImage(image, FACTOR));
                ImageEditor.saveImage(logger, compilation, filename);

            } else {
                ImageEditor.saveImage(logger, getScaledImage(image, FACTOR), filename);
            }

        } catch (IOException e) {
            logger.error("Failed to open existing image, cant append new image");
            return;
        }
        logger.info("Appended screenshot with text '" + text + "' to file: " + filename + " in " + stopwatch);
        stopwatch.reset();
    }

    private static BufferedImage getScaledImage(BufferedImage src, double factor) {
        int finalw = (int) (src.getWidth() * factor);
        int finalh = (int) (src.getHeight() * factor);
        BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return resizedImg;
    }

    private static BufferedImage joinBufferedImageHorizontal(BufferedImage img1, BufferedImage img2) {
        int offset = 5;
        int wid = img1.getWidth() + img2.getWidth() + offset;
        int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
        BufferedImage newImage = new BufferedImage(wid, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth() + offset, 0);
        g2.dispose();
        return newImage;
    }

    private static BufferedImage joinBufferedImageVertical(BufferedImage img1, BufferedImage img2) {
        int offset = 5;
        int width = Math.max(img1.getWidth(), img2.getWidth()) + offset;
        int height = img1.getHeight() + img2.getHeight() + offset;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight() + offset);
        g2.dispose();
        return newImage;
    }

    public static void saveImage(Logger logger, BufferedImage image, String path) {
        try {
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.createGraphics().drawImage(image, 0, 0, Color.white, null);
            ImageIO.write(rgbImage, FILE_FORMAT, new File(path));
        } catch (IOException e) {
            String[] fixes = {"Verify folder is valid", "Verify you have permissions to write to folder"};
            Errors.logError(logger, "IOException - Failed to save image to path: " + path, fixes);
        }
    }

    public static ImageComparisonData compareImages(Logger logger, int[]image1Data, int[]image2Data, int w, int h) {
        int totalDiffValue = 0;
        ImageComparisonData comparisonData;
        int totalDiffRed = 0;
        int totalDiffBlue = 0;
        int totalDiffGreen = 0;
        try {
            if (image1Data.length == image2Data.length)
            {
                for (int y = 0; y < image1Data.length; y++)
                {
                    int red1 = (image1Data[y] >> 16) & 0xff;
                    int red2 = (image2Data[y] >> 16) & 0xff;
                    int green1 = (image1Data[y] >> 8) & 0xff;
                    int green2 = (image2Data[y] >> 8) & 0xff;
                    int blue1 = image1Data[y] & 0xff;
                    int blue2 = image2Data[y] & 0xff;
                    totalDiffValue += Math.abs(red1-red2) + Math.abs(green1-green2) + Math.abs(blue1-blue2);
                    totalDiffRed += Math.abs(red1-red2);
                    totalDiffGreen += Math.abs(green1-green2);
                    totalDiffBlue += Math.abs(blue1-blue2);
                }
            int maxPixelDiffValue = 3 * 255 * w * h;
            int maxSingleColorDiffValue = 255 * w * h;
            float similarityPercentage = 100f - (100.0f * totalDiffValue / maxPixelDiffValue);
            float similarityPercentage_red = 100f - (100.0f * totalDiffRed / maxSingleColorDiffValue);
            float similarityPercentage_green = 100f - (100.0f * totalDiffGreen / maxSingleColorDiffValue);
            float similarityPercentage_blue = 100f - (100.0f * totalDiffBlue / maxSingleColorDiffValue);
            comparisonData = new ImageComparisonData(similarityPercentage,similarityPercentage_red,similarityPercentage_green,similarityPercentage_blue,2);
        } else {
                throw new SkipException("The two images does not contain same amount of pixels(" + image1Data.length + " vs " + image2Data.length + ") and could not be compared, skipping test. " +
                        "(There is currently an issue comparing images with different scroll bar existence on Firefox and Safari, respective driver crops out any potential scroll bar resulting in " +
                        "different image resolutions and non comparable images)");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to compare the given image files: " + e.getMessage());
        }
        return comparisonData;
    }

    public static float compareImages(Logger logger, File file1, File file2) {
        float similarityPercentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage image1 = ImageIO.read(file1);
            DataBuffer image1DataBuffer = image1.getData().getDataBuffer();
            float sizeImage1 = image1DataBuffer.getSize();
            BufferedImage image2 = ImageIO.read(file2);
            DataBuffer image2DataBuffer = image2.getData().getDataBuffer();
            float sizeImage2 = image2DataBuffer.getSize();
            int similarityCount = 0;

            // compare data-buffer objects //
            if (sizeImage1 == sizeImage2) {
                for (int i = 0; i < sizeImage1; i++) {
                    if (image1DataBuffer.getElem(i) == image2DataBuffer.getElem(i)) {
                        similarityCount++;
                    }
                }
                similarityPercentage = (similarityCount * 100.0f) / sizeImage1;
                double twoDecimalRoundedSimilarityPercentage =  Math.round(similarityPercentage * Math.pow(10, 2)) / Math.pow(10, 2);
                similarityPercentage = (float)twoDecimalRoundedSimilarityPercentage;
            } else {
                throw new SkipException("The two images are not the same size (" + sizeImage1 + " vs " + sizeImage2 + ") and could not be compared, skipping test. " +
                        "(There is currently an issue comparing images with different scroll bar existence on Firefox and Safari, respective driver crops out any potential scroll bar resulting in " +
                        "different image resolutions and non comparable images)");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to compare the given image files: " + e.getMessage());
        }
        if(similarityPercentage > 100.0f) similarityPercentage = 100.0f;
        return similarityPercentage;
    }

    public static void setScreenshotSizeFactor(Logger logger, double factor) {
        if(factor < MINIMUM_FACTOR) {
            logger.trace("Tried to set screenshot size factor below minimum allowed value, defaulting to size factor " + MINIMUM_FACTOR);
            factor = MINIMUM_FACTOR;
        } else if(factor > MAXIMUM_FACTOR) {
            logger.trace("Tried to set screenshot size factor above maximum allowed value, defaulting to size factor " + MAXIMUM_FACTOR);
            factor = MAXIMUM_FACTOR;
        }
        FACTOR = factor;
    }

    public static void setScreenshotFileFormat(Logger logger, String fileFormat) {
        fileFormat.toLowerCase();
        if(SUPPORTED_FORMATS.contains(fileFormat)) {
            FILE_FORMAT = fileFormat;
        } else {
            logger.warn("Illegal screenshot file format \"" + fileFormat + "\", defaulting to jpg");
        }
    }
}
