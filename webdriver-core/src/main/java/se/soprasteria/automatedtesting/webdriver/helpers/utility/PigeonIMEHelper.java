package se.soprasteria.automatedtesting.webdriver.helpers.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseclass.Execution;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides functions for PigeonIME which is used as
 * automation keyboard on Android devices
 */
public class PigeonIMEHelper {

    private static final Logger logger = LogManager.getLogger("PigeonIME");

    //Application and ADB command constants
    private static final String ADB_TEXT_COMMAND = "PIGEON_INPUT_TEXT";
    private static final String PIGEON_IME_PACKAGE_NAME = "se.soprasteria.pigeonime";
    private static final String PIGEON_IME_MAIN_ACTIVITY = "PigeonIME";

    //Filepath constants
    private static final String PIGEON_IME_APK_URL = "https://github.com/mattiasfloden/PigeonIME/raw/master/PigeonIME.apk"; //TODO: Change to Pigeon github repository
    private static final String PIGEON_IME_APK_DOWNLOAD_PATH = System.getProperty("user.dir") + "/target/PigeonIME.apk";

    private String deviceName;

    /**
     * Initialize the PigeonIME keyboard on android device
     *
     * @param deviceName ADB name of the device to initialize PigeonIME on
     */
    public PigeonIMEHelper(String deviceName) {
        this.deviceName = deviceName;
        if (deviceName == null)
            try {
                throw new IOException("deviceName not found in desired capabilities");
            } catch (IOException e) {
                logger.error(e.getMessage());
                return;
            }
        initPigeonIME();
    }


    /**
     * Function that initializes PigeonIME on the Android device
     */
    private void initPigeonIME() {
        if (!isPigeonIMEInstalled()) {
            if (!new File(PIGEON_IME_APK_DOWNLOAD_PATH).exists()) {
                downloadPigeonAPK();
            }
            installPigeonIME();
            Execution.sleep(1000, logger, null); //Wait for APK to initialize on device
        }
        setPigeonImeAsKeyboard();
    }


    /**
     * ADB command that installs PigeonIME on the Android device
     */
    private void installPigeonIME() {
        logger.info("Installing PigeonIME on device");
        List<String> installAPKScript = Arrays.asList(
                "adb", "-s", deviceName, "install", PIGEON_IME_APK_DOWNLOAD_PATH
        );
        ProcessBuilder builder = new ProcessBuilder(installAPKScript);
        builder.redirectErrorStream(true);
        try {
            Process installAPK = builder.start();
            installAPK.waitFor();
            String response = readProcessOutput(installAPK);
            if (!response.contains("Success"))
                logger.error("Failed to install PigeonIME: " + response);

        } catch (IOException e) {
            logger.error("Failed to execute install PigeonIME command: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Failed to wait for command to execute: " + e.getMessage());
        }
    }

    /**
     * Method that downloads the PigeonIME.apk file the target folder on the local machine
     */
    private void downloadPigeonAPK() {
        logger.info("Downloading PigeonIME.apk");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(PIGEON_IME_APK_URL).openStream());
             FileOutputStream fileOS = new FileOutputStream(PIGEON_IME_APK_DOWNLOAD_PATH)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            logger.error("Failed to download PigeonIME.apk: " + e.getMessage());
        }
    }


    /**
     * ADB command that sets PigeonIME as the default keyboard the Android device
     */
    private void setPigeonImeAsKeyboard() {
        logger.info("Setting PigeonIME as default keyboard");
        List<String> setPigeonImeAsKeyboardScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "ime", "set", PIGEON_IME_PACKAGE_NAME + "/." + PIGEON_IME_MAIN_ACTIVITY
        );
        ProcessBuilder builder = new ProcessBuilder(setPigeonImeAsKeyboardScript);
        builder.redirectErrorStream(true);
        try {
            Process setPigeonIME = builder.start();
            setPigeonIME.waitFor();
            String response = readProcessOutput(setPigeonIME);
            if (!response.contains("selected"))
                logger.error("Failed to set PigeonIME as default keyboard: " + response);
        } catch (IOException e) {
            logger.error("Failed to execute change keyboard command: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Failed to wait for command to execute: " + e.getMessage());
        }
    }

    /**
     * Method that executes an ADB script that checks if PigeonIME is installed
     * on the Android device.
     *
     * @return Returns true if PigeonIME is installed on the device
     */
    private boolean isPigeonIMEInstalled() {
        List<String> isPigeonIMEInstalledScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "pm", "list", "package " + PIGEON_IME_PACKAGE_NAME
        );
        ProcessBuilder builder = new ProcessBuilder(isPigeonIMEInstalledScript);
        builder.redirectErrorStream(true);
        try {
            Process isPigeonIMEInstalled = builder.start();
            isPigeonIMEInstalled.waitFor();
            String response = readProcessOutput(isPigeonIMEInstalled);
            if (response.contains("package:" + PIGEON_IME_PACKAGE_NAME)) {
                return true;
            }
        } catch (IOException e) {
            logger.error("Failed to execute isPigeonIMEInstalled command: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Failed to wait for command to execute: " + e.getMessage());
        }
        return false;
    }


    /**
     * Method that returns the response message of ProcessBuilder command as a String
     *
     * @param process Process to read output message from
     * @return Response message of process
     */
    private String readProcessOutput(Process process) {
        String result = "";
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder outputBuilder = new StringBuilder();
        String line = null;
        try {
            while (true) {
                if (!((line = reader.readLine()) != null)) break;
                outputBuilder.append(line);
                outputBuilder.append(System.getProperty("line.separator"));
            }
            result = outputBuilder.toString();
        } catch (IOException e) {
            logger.error("Failed to read command output: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("Failed to close command reader: " + e.getMessage());
            }
        }
        return result;
    }


    /**
     * Send a broadcast command to PigeonIME installed on an Android device using ADB shell command
     * to simulate keystrokes.
     *
     * @param deviceName            name of Android device to send command to
     * @param searchString          text to be written by the keyboard
     * @param millisBetweenKeypress time in ms between each simulated keypress
     */
    public static void sendKeysAndroidWithControlledSpeed(String deviceName, String searchString, int millisBetweenKeypress) {
        List<String> sendKeysAndroidScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "am broadcast", "-a " + ADB_TEXT_COMMAND,
                "--es", "msg " + "'" + searchString + "'",
                "--ei", "dt " + millisBetweenKeypress
        );

        ProcessBuilder builder = new ProcessBuilder(sendKeysAndroidScript);
        builder.redirectErrorStream(true);
        try {
            Process sendText = builder.start();
            sendText.waitFor();
        } catch (IOException e) {
            System.err.println("Failed to execute ADB command: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Failed to wait for command to execute: " + e.getMessage());
        }
    }
}

