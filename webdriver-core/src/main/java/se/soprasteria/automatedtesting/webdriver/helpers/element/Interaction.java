package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.PigeonIMEHelper;

import static se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption.ANDROID_VIRTUAL_KEYBOARD;


public class Interaction extends PigeonIMEHelper {

    private final AutomationDriver driver;

    public Interaction(AutomationDriver driver) {
        this.driver = driver;
    }

    public void sendKeysWithControlledSpeed(WebElement element, String searchString, int millisBetweenKeypress) {
        if (driver.isAndroid()) {
            boolean androidVirtualKeyboard = Boolean.valueOf(BaseTestConfig.getConfigurationOption(ANDROID_VIRTUAL_KEYBOARD));
            if (androidVirtualKeyboard) {
                element.click();
                sendKeysAndroidWithControlledSpeed(driver.getCapability("udid"), searchString, millisBetweenKeypress);
            } else {
                element.sendKeys(searchString);
            }
        } else {
            for (char c : searchString.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                sleep(millisBetweenKeypress);
            }
        }
    }
}
