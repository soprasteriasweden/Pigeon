package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.PigeonIMEHelper;


public class Interaction extends PigeonIMEHelper {


    private final AutomationDriver driver;

    public Interaction(AutomationDriver driver) {
        this.driver = driver;
    }


    public void sendKeysWithControlledSpeed(WebElement element, String searchString, int millisBetweenKeypress) {
        if (driver.isAndroid()) {
            element.click();
            sendKeysAndroidWithControlledSpeed(driver.getCapability("udid"), searchString, millisBetweenKeypress);
        } else {
            for (char c : searchString.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                sleep(millisBetweenKeypress);
            }
        }
    }
}
