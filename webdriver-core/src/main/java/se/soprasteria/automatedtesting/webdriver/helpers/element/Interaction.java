package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

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
}
