package se.soprasteria.automatedtesting.webdriver.helpers.video;

import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.screenrecording.ScreenRecordingUploadOptions;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;
import static org.monte.media.FormatKeys.MediaType;

/**
 *
 * VIDEO RECORDING FUNCTIONALITY IS A BETA FEATURE!
 * IT IS UNSTABLE AND NEED MORE TESTING.
 *
 * Class with methods handling the video recording functionality.
 * If property "config.video.record" is set to value "true" in config.xml-file
 * video recording will be saved whenever a test fails.
 *
 */

public class VideoRecording extends BaseClass {

    private ScreenRecorder screenRecorder;
    public static final String USER_DIR = "user.dir";
    private static String outBaseFolder = "target/surefire-reports/video/";
    private String folder;
    private String currentVideoFilePath;
    private static final VideoRecording INSTANCE;
    private static final boolean VIDEO_RECORDING_ENABLED;

    static {
        INSTANCE = new VideoRecording();
        VIDEO_RECORDING_ENABLED = Boolean.valueOf(BaseTestConfig.getConfigurationOption(ConfigurationOption.VIDEO_RECORDING));
    }
    
    public static VideoRecording getInstance() {
        return INSTANCE;
    }

    public void startRecording(AutomationDriver driver) {
        if (VIDEO_RECORDING_ENABLED) {
            initVideoRecording();
            if(driver.isWeb() || driver.isWindowsDriver()) {
                startRecordingWeb();
            } else if(driver.isMobile()) {
                startRecordingMobile(driver);
            }
        }
    }

    public void stopRecording(AutomationDriver driver, boolean deleteRecording, String testName) {
        if (VIDEO_RECORDING_ENABLED) {
            if(driver.isWeb() || driver.isWindowsDriver()) {
                stopRecordingWeb(testName);
            } else if(driver.isMobile()) {
                stopRecordingMobile(driver, testName);
            }
            logger.info("Stopped video recording");
        }
        if(deleteRecording) deleteRecording();
    }

    private void startRecordingWeb() {
        try {
            logger.info("Video recording is ON, Video folder: " + folder);
            this.screenRecorder = new ScreenRecorder(getGraphicsConfiguration(), null,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    null, null, new File(folder));
            this.screenRecorder.start();
            logger.info("Start Video recording");
        } catch (Exception e) {
            System.setProperty("videoRecord", null);
            logger.error("Can't start video recording." + e.getMessage());
        }
    }

    private void startRecordingMobile(AutomationDriver driver) {

        BaseStartScreenRecordingOptions options;
        if(driver.isAndroid()) {
            options = new AndroidStartScreenRecordingOptions();
        }
        else {
            options = new IOSStartScreenRecordingOptions();
        }
        try {
            ((CanRecordScreen)driver.getWebDriver()).startRecordingScreen(options);
        } catch (Exception e) {
            logger.warn("Could not start recording screen on mobile platform: " + e.getMessage());
        }
        logger.trace("Mobile recording started");
    }

    private void stopRecordingWeb(String testName) {
        try {
            this.screenRecorder.stop();
            File currentVideoFile = this.screenRecorder.getCreatedMovieFiles().get(0);
            currentVideoFilePath = videoFileName(testName) + ".avi";
            File videoFile = new File(currentVideoFilePath);
            currentVideoFile.renameTo(videoFile);
            logger.info("Append video record to file : " + currentVideoFile);
        } catch (Exception e) {
            logger.error("Can't stop video recording." + e.getMessage());
        }
    }

    private void stopRecordingMobile(AutomationDriver driver, String testName) {
        BaseStopScreenRecordingOptions options;
        if(driver.isAndroid()) {
            options = new AndroidStopScreenRecordingOptions();
        }
        else {
            options = new IOSStopScreenRecordingOptions();
        }
        byte[] decodedBytes = null;
        try {
            decodedBytes = Base64.getDecoder().decode(((CanRecordScreen)driver.getWebDriver()).stopRecordingScreen(options));
        } catch (Exception e) {
            logger.warn("Could not stop recording screen on mobile platform: " + e.getMessage());
        }
        try {
            currentVideoFilePath = videoFileName(testName) + ".mp4";
            FileOutputStream out = new FileOutputStream(currentVideoFilePath);
            out.write(decodedBytes);
            out.close();
        } catch (Exception e) {
            logger.error("Could not decode mobile video: ", e.toString());
        }
        logger.trace("Mobile recording stopped");
    }

    private void initVideoRecording() {
        folder = System.getProperty(USER_DIR) + File.separator + outBaseFolder;
        File movieFolder = new File(folder);
        if (!movieFolder.exists()) {
            movieFolder.mkdir();
        }
    }

    private void deleteRecording() {
        try {
            Path path = Paths.get(currentVideoFilePath);
            Files.delete(path);
            logger.info("Test passed, deleting file : " + currentVideoFilePath);
        } catch (Exception ioe) {
            logger.error("File : " + currentVideoFilePath + " could not be deleted: " + ioe.getMessage());
        }
    }

    private GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    }

    private String videoFileName(String testName) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy_HH-mm-ss");
        return folder + testName + "_" + simpleDateFormat.format(date.getTime());
    }
}
