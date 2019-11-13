package se.soprasteria.automatedtesting.webdriver.helpers.utility;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseclass.Execution;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides functions for PigeonIME which is used as
 * automation keyboard on Android devices
 */
public class PigeonIMEHelper extends BaseClass {

    //Application and ADB command constants
    private static final String ADB_TEXT_COMMAND = "PIGEON_INPUT_TEXT";
    private static final String PIGEON_IME_PACKAGE_NAME = "se.soprasteria.pigeonime";
    private static final String PIGEON_IME_MAIN_ACTIVITY = "PigeonIME";

    //Filepath constants
    private static final String PIGEON_IME_APK_URL = "https://github.com/soprasteriasweden/Pigeon/raw/master/webdriver-core/src/main/resources/PigeonIME.apk";
    private static final String PIGEON_IME_APK_DOWNLOAD_PATH = System.getProperty("user.dir") + "/src/main/resources/PigeonIME.apk";


    /**
     * Initialize the PigeonIME keyboard on android device
     *
     * @param deviceName ADB name of the device to initialize PigeonIME on
     */
    public PigeonIMEHelper(String deviceName) throws IOException {
        if (deviceName != null) {
            initPigeonIME(deviceName);
        } else {
            throw new IOException("deviceName not found in desired capabilities");
        }
    }

    public PigeonIMEHelper() {
    }


    /**
     * Function that initializes PigeonIME on the Android device
     *
     * @param deviceName
     */
    private void initPigeonIME(String deviceName) throws IOException {
        if (!isPigeonIMEInstalled(deviceName)) {
            if (!new File(PIGEON_IME_APK_DOWNLOAD_PATH).exists()) {
                downloadPigeonAPK();
            }
            installPigeonIME(deviceName);
            Execution.sleep(1000, logger, null); //Wait for APK to initialize on device
        }
        setPigeonImeAsKeyboard(deviceName);
    }


    /**
     * ADB command that installs PigeonIME on the Android device
     *
     * @param deviceName ADB name of the device to install PigeonIME on
     */
    private void installPigeonIME(String deviceName) throws IOException {
        logger.info("Installing PigeonIME on device");
        List<String> installAPKScript = Arrays.asList(
                "adb", "-s", deviceName, "install", PIGEON_IME_APK_DOWNLOAD_PATH
        );
        ProcessBuilder builder = new ProcessBuilder(installAPKScript);
        builder.redirectErrorStream(true);
        Process installAPK = builder.start();
        try {
            installAPK.waitFor();
        } catch (InterruptedException e) {
            logger.error("Failed to wait for process to finish");
        }
        String response = readProcessOutput(installAPK);
        if (!response.contains("Success")) {
            logger.error("Failed to install PigeonIME: " + response);
            throw new IOException("PigeonIME.apk failed to install correctly");
        }
    }

    /**
     * Method that downloads the PigeonIME.apk file the target folder on the local machine
     */
    private void downloadPigeonAPK() throws IOException {
        logger.info("Downloading PigeonIME.apk");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(PIGEON_IME_APK_URL).openStream());
             FileOutputStream fileOS = new FileOutputStream(PIGEON_IME_APK_DOWNLOAD_PATH)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        }
    }


    /**
     * ADB command that sets PigeonIME as the default keyboard the Android device
     *
     * @param deviceName ADB name of the device to set PigeonIME ad keyboard on
     */
    private void setPigeonImeAsKeyboard(String deviceName) throws IOException {
        logger.info("Setting PigeonIME as default keyboard");
        List<String> setPigeonImeAsKeyboardScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "ime", "set", PIGEON_IME_PACKAGE_NAME + "/." + PIGEON_IME_MAIN_ACTIVITY
        );
        ProcessBuilder builder = new ProcessBuilder(setPigeonImeAsKeyboardScript);
        builder.redirectErrorStream(true);
        Process setPigeonIME = builder.start();
        try {
            setPigeonIME.waitFor();
        } catch (InterruptedException e) {
            logger.error("Failed to wait for process to finish");
        }
        String response = readProcessOutput(setPigeonIME);
        if (!response.contains("selected"))
            logger.error("Failed to set PigeonIME as default keyboard: " + response);
    }

    /**
     * Method that executes an ADB script that checks if PigeonIME is installed
     * on the Android device.
     *
     * @param deviceName ADB name of the device to check if PigeonIME is installed
     * @return Returns true if PigeonIME is installed on the device
     */
    private boolean isPigeonIMEInstalled(String deviceName) throws IOException {
        List<String> isPigeonIMEInstalledScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "pm", "list", "package " + PIGEON_IME_PACKAGE_NAME
        );
        ProcessBuilder builder = new ProcessBuilder(isPigeonIMEInstalledScript);
        builder.redirectErrorStream(true);

        Process isPigeonIMEInstalled = builder.start();
        try {
            isPigeonIMEInstalled.waitFor();
        } catch (InterruptedException e) {
            logger.error("Failed to wait for process to finish");
        }
        String response = readProcessOutput(isPigeonIMEInstalled);
        if (response.contains("package:" + PIGEON_IME_PACKAGE_NAME)) {
            return true;
        }
        return false;
    }


    /**
     * Method that returns the response message of ProcessBuilder command as a String
     *
     * @param process Process to read output message from
     * @return Response message of process
     */
    private String readProcessOutput(Process process) throws IOException {
        String result = "";
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))
        ) {
            StringBuilder outputBuilder = new StringBuilder();
            String line = null;
            while (true) {
                if (!((line = reader.readLine()) != null)) break;
                outputBuilder.append(line);
                outputBuilder.append(System.getProperty("line.separator"));
            }
            result = outputBuilder.toString();

            return result;
        }
    }


    /**
     * Send a broadcast command to PigeonIME installed on an Android device using ADB shell command
     * to simulate keystrokes.
     *
     * @param deviceName            name of Android device to send command to
     * @param searchString          text to be written by the keyboard
     * @param millisBetweenKeypress time in ms between each simulated keypress
     */
    public void sendKeysAndroidWithControlledSpeed(String deviceName, String searchString, int millisBetweenKeypress) {
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
            logger.error("Failed to execute ADB command: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Failed to wait for command to execute: " + e.getMessage());
        }
    }
}

