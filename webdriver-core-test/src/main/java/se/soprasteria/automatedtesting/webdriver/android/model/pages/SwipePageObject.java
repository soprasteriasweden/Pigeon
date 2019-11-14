package se.soprasteria.automatedtesting.webdriver.android.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class SwipePageObject extends BasePageObject {

    @FindBy(id = "switchToButtons")
    WebElement buttonSwitchToButtonsScreen;
    @FindBy(id = "swipeUp")
    WebElement textSwipedUp;
    @FindBy(id = "swipeDown")
    WebElement textSwipedDown;
    @FindBy(id = "swipeLeft")
    WebElement textSwipedLeft;
    @FindBy(id = "swipeRight")
    WebElement textSwipedRight;

    public SwipePageObject(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(buttonSwitchToButtonsScreen, 5000) &&
                elementHelper.isElementGoneWithinTime(textSwipedUp, 1000) &&
                elementHelper.isElementGoneWithinTime(textSwipedDown, 1000) &&
                elementHelper.isElementGoneWithinTime(textSwipedLeft, 1000) &&
                elementHelper.isElementGoneWithinTime(textSwipedRight, 1000);
    }

    public void switchToButtonsScreen() {
        elementHelper.clickWithinTime(buttonSwitchToButtonsScreen, 2000);
    }

    public boolean isSwipeUpDisplayed() {
        return elementHelper.isElementDisplayedWithinTime(textSwipedUp, 0);
    }

    public boolean isSwipeDownDisplayed() {
        return elementHelper.isElementDisplayedWithinTime(textSwipedDown, 0);
    }

    public boolean isSwipeLeftDisplayed() {
        return elementHelper.isElementDisplayedWithinTime(textSwipedLeft, 0);
    }

    public boolean isSwipeRightDisplayed() {
        return elementHelper.isElementDisplayedWithinTime(textSwipedRight, 0);
    }

    public void swipeUp() {
        driver.swipeUp(50, 30, Speed.VERY_FAST);
        logger.info("Successfully swiped up");
    }

    public void swipeDown() {
        driver.swipeDown(50, 30, Speed.VERY_FAST);
        logger.info("Successfully swiped down");
    }

    public void swipeLeft() {
        driver.swipeLeft(50, 70, Speed.VERY_FAST);
        logger.info("Successfully swiped left");
    }

    public void swipeRight() {
        driver.swipeRight(50, 70, Speed.VERY_FAST);
        logger.info("Successfully swiped right");
    }
}
