package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.util.Arrays;
import java.util.List;


public class Interaction extends BaseClass {


    private final AutomationDriver driver;

    public Interaction(AutomationDriver driver) {
        this.driver = driver;
    }

    public void sendKeysWithControlledSpeed(WebElement element, String searchString, int millisBetweenKeypress) {
        for (int i = 0; i < searchString.length(); i++) {
            element.sendKeys(searchString.substring(i, i + 1));
            sleep(millisBetweenKeypress);
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    public void sendKeysWithControlledSpeed2(WebElement element, String searchString, int millisBetweenKeypress) {
        if (driver.isAndroid()) {
            sendKeysAndroidKeyboardWithControlledSpeed(element, searchString, millisBetweenKeypress);
        } else if (driver.isIos()) {
            element.sendKeys(searchString);
        } else {
            for (char c : searchString.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                sleep(millisBetweenKeypress);
            }
        }
    }

    /*
     * To be able to simulate android keys one-by-one the keys are sent as a broadcast message
     * to a virtual keyboard. The broadcast message is sent using ADB shell-command
     */
    private void sendKeysAndroidKeyboardWithControlledSpeed(WebElement element, String searchString, int millisBetweenKeypress) {
        String adbTextCommand = "PIGEON_INPUT_TEXT";
        String deviceName = driver.getCapability("deviceName");
        List<String> sendKeysAndroidScript = Arrays.asList(
                "adb", "-s", deviceName, "shell", "am broadcast", "-a " + adbTextCommand,
                "--es", "msg " + "'" + searchString + "'",
                "--ei", "dt " + millisBetweenKeypress
        );
        element.click();
        ProcessBuilder builder = new ProcessBuilder(sendKeysAndroidScript);
        builder.redirectErrorStream(true);
        try {
            Process sendText = builder.start();
        } catch (Exception e) {
            logger.trace("Failed to execute ADB command" + e.getMessage());
        }
        sleep(millisBetweenKeypress * searchString.length());
    }
}
