package se.soprasteria.automatedtesting.webdriver.android.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class ElementPage extends BasePageObject {
    @FindBy(id = "buttonShowElement")
    WebElement buttonShowElement;
    @FindBy(id = "textShowElement")
    WebElement textShowElement;
    @FindBy(id = "buttonShowElementDelay")
    WebElement buttonShowElementDelay;
    @FindBy(id = "textShowElementDelay")
    WebElement textShowElementDelay;
    @FindBy(id = "buttonHideElement")
    WebElement buttonHideElement;
    @FindBy(id = "textHideElement")
    WebElement textHideElement;
    @FindBy(id = "buttonHideElementDelay")
    WebElement buttonHideElementDelay;
    @FindBy(id = "textHideElementDelay")
    WebElement textHideElementDelay;
    @FindBy(id = "checkBox")
    WebElement checkBox;
    @FindBy(id = "textCheckBox")
    WebElement textCheckBox;
    @FindBy(id = "resetButton")
    WebElement resetButton;

    static final int DELAY_MILLIS_LONG = 5000;
    static final int DELAY_MILLIS = 2500;
    static final int DELAY_MILLIS_SHORT = 1000;


    public ElementPage(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        if (elementHelper.isElementPresentAndDisplayedWithinTime(resetButton, DELAY_MILLIS_LONG)) {
            logger.info("ElementPage loaded correctly");
            return true;
        }
        logger.error("ElementPage failed to load correctly");
        return false;
    }

    public boolean showElement() {
        logger.info("Clicking \"SHOW ELEMENT\" and verifies that element becomes visible");
        elementHelper.clickWithinTime(buttonShowElement, DELAY_MILLIS_SHORT);
        if (elementHelper.isElementPresentAndDisplayed(textShowElement)) {
            logger.info("Element is present and displayed");
            return true;
        }
        logger.error("Element is not present and displayed");
        return false;
    }

    public boolean showElementDelay() {
        logger.info("Clicking \"SHOW ELEMENT DELAY\" and verifies that element becomes visible after delay");
        elementHelper.clickWithinTime(buttonShowElementDelay, DELAY_MILLIS_SHORT);
        if (elementHelper.isElementPresentAndDisplayedWithinTime(textShowElement, DELAY_MILLIS)) {
            logger.info("Element is present and displayed after delay");
            return true;
        }
        logger.error("Element is not present and displayed after delay");
        return false;
    }

    public boolean hideElement() {
        logger.info("Clicking \"HIDE ELEMENT\" and verifies that element becomes hidden");
        elementHelper.clickWithinTime(buttonHideElement, DELAY_MILLIS_SHORT);
        return elementHelper.isElementGoneWithinTime(textHideElement, DELAY_MILLIS_SHORT);
    }

    public boolean hideElementDelay() {
        logger.info("Clicking \"HIDE ELEMENT DELAY\" and verifies that element becomes hidden after delay");
        elementHelper.clickWithinTime(buttonHideElementDelay, DELAY_MILLIS_SHORT);
        return elementHelper.isElementHiddenOrGoneWithinTime(textShowElement, DELAY_MILLIS);
    }

    public boolean clickCheckBox() {
        logger.info("Clicking check box and verifies that it's enabled");
        elementHelper.clickWithinTime(checkBox, DELAY_MILLIS_SHORT);
        if (checkBox.isEnabled()) {
            logger.info("Check box was enabled correctly");
            return true;
        }
        logger.error("Check box was not enabled correctly");
        return false;
    }

}
