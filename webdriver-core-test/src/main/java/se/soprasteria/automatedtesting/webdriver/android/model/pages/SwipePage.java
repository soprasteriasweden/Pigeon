package se.soprasteria.automatedtesting.webdriver.android.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class SwipePage extends BasePageObject {

    @FindBy(id = "swipeInfo")
    WebElement swipeInfo;

    static final String SWIPE_UP = "Swiped up!";
    static final String SWIPE_DOWN = "Swiped down!";
    static final String SWIPE_RIGHT = "Swiped right!";
    static final String SWIPE_LEFT = "Swiped left!";

    public SwipePage(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementPresentAndDisplayedWithinTime(swipeInfo, 5000);
    }

    public boolean swipeUp(){
        driver.swipeUp(50, 30, Speed.VERY_FAST);
        if(elementHelper.isTextPresentInElementWithinTime(swipeInfo, SWIPE_UP, 5000)) {
            logger.info("Successfully swiped up");
            return true;
        }
        logger.error("Failed to swipe up");
        return false;
    }

    public boolean swipeDown(){
        driver.swipeDown(50, 30, Speed.VERY_FAST);
        if(elementHelper.isTextPresentInElementWithinTime(swipeInfo, SWIPE_DOWN, 5000)) {
            logger.info("Successfully swiped down");
            return true;
        }
        logger.error("Failed to swipe down");
        return false;
    }

    public boolean swipeRight(){
        driver.swipeRight(50, 30, Speed.VERY_FAST);
        if(elementHelper.isTextPresentInElementWithinTime(swipeInfo, SWIPE_RIGHT, 5000)) {
            logger.info("Successfully swiped right");
            return true;
        }
        logger.error("Failed to swipe right");
        return false;
    }

    public boolean swipeLeft(){
        driver.swipeLeft(50, 30, Speed.VERY_FAST);
        if(elementHelper.isTextPresentInElementWithinTime(swipeInfo, SWIPE_LEFT, 5000)) {
            logger.info("Successfully swiped left");
            return true;
        }
        logger.error("Failed to swipe left");
        return false;
    }
}
