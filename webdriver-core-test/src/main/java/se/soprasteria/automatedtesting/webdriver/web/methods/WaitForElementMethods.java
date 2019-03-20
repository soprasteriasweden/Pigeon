package se.soprasteria.automatedtesting.webdriver.web.methods;

import com.google.common.base.Stopwatch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.datastructure.ElementState;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;

import java.util.concurrent.TimeUnit;

import static se.soprasteria.automatedtesting.webdriver.web.datastructure.ElementState.ELEMENT_WILL_BE_DISPLAYED;

public class WaitForElementMethods extends MainPage {

    public WaitForElementMethods(AutomationDriver driver) {
        super(driver);
    }

    public boolean waitForElementToChangeState(ElementState state, int timeOutMillis, int elementDelayTime) {
        final long ACCEPTED_DIFFERENCE = 1000;
        String message = "", errorMessage = "";

        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            switch (state){
                case ELEMENT_WILL_BE_DISPLAYED:
                    message = "Method: WaitForElementToBeDisplayed() does not seem to have stopped timeout around the same time as element was clickable.";
                    errorMessage = "Element was never displayed";
                    elementHelper.waitForElementToBeDisplayed(delayedElement, timeOutMillis);
                    break;
                case ELEMENT_WILL_BE_REMOVED:
                    message = "Method: WaitForElementToBeGone() does not seem to have stopped timeout around the same time as element was removed.";
                    errorMessage = "Element was never removed";
                    elementHelper.waitForElementToBeGone(delayedRemovedElement, timeOutMillis);
                    break;
                case ELEMENT_WILL_BE_CLICKABLE:
                    message = "Method: WaitForElementToBeClickable() does not seem to have stopped timeout around the same time as element was clickable.";
                    errorMessage = "Element was never clickable";
                    elementHelper.waitForElementToBeClickable(delayedElement, timeOutMillis);
                    break;
                case ELEMENT_WILL_BE_HIDDEN:
                    message = "Method: WaitForElementToBeClickable() does not seem to have stopped timeout around the same time as element was hidden.";
                    errorMessage = "Element was never hidden";
                    elementHelper.waitForElementToBeHidden(delayedHiddenElement, timeOutMillis);
                    break;
                case TEXT_WILL_BE_PRESENT:
                    message = "Method: WaitForTextToBePresentInElement() does not seem to have stopped timeout around the same time as text was present in element.";
                    errorMessage = "Text was never present in element";
                    elementHelper.waitForTextToBePresentInElement(text, EXAMPLE_STRING,timeOutMillis);
                    break;
                case NESTED_ELEMENT_WILL_BE_DISPLAYED:
                    message = "Method: WaitForNestedElementToBeDisplayed() does not seem to have stopped timeout around the same time as element was displayed.";
                    errorMessage = "Element was never displayed";
                    elementHelper.waitForNestedElementToBeDisplayed(parentElement, By.className("child-ul"),timeOutMillis);
                    break;
                default:
                    break;
            }
            stopwatch.stop();
            long resultTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            if (resultTime <= elementDelayTime + ACCEPTED_DIFFERENCE && resultTime < timeOutMillis) {
                return true;
            } else {
                logger.info(message);
                return false;
            }
        } catch (WebDriverException e) {
            logger.info(errorMessage);
            return false;
        }
    }

    public boolean waitForElementToBePresentAndDisplayed(ElementState state, boolean locateWithBy, int timeOutMillis, int elementDelayTime) {
        final long ACCEPTED_DIFFERENCE = 5000;
        String message = "";
        String errorMessage = "";
        long resultTime;
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            if (state == ELEMENT_WILL_BE_DISPLAYED) {
                if (locateWithBy) {
                    elementHelper.waitForElementToBePresentAndDisplayed(By.id("createdelement"), timeOutMillis);
                } else {
                    elementHelper.waitForElementToBePresentAndDisplayed(createdAndDisplayedElement, timeOutMillis);
                }
            } else {
                if (locateWithBy) {
                    elementHelper.waitForElementToBePresentAndDisplayed(By.id("createdhiddenbutton"), timeOutMillis);
                } else {
                    elementHelper.waitForElementToBePresentAndDisplayed(createdAndHiddenElement, timeOutMillis);
                }
            }
            stopwatch.stop();
            resultTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (resultTime <= elementDelayTime + ACCEPTED_DIFFERENCE && resultTime < timeOutMillis) {
                return true;
            } else {
                logger.info("Method: WaitForNestedElementToBeDisplayed() does not seem to have stopped timeout around the same time as element was displayed.");
                return false;
            }
        } catch (WebDriverException e) {
            logger.info("Element was not in an expected state");
            return false;
        }
    }

    public boolean waitForElementToBeDisplayedAndRefreshed( int timeOutMillis, int elementDelayTime) {
        final long ACCEPTED_DIFFERENCE = 1000;
        long resultTime;
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            elementHelper.waitForElementToBeDisplayedAndRefreshed(hiddenDisabledButton, timeOutMillis);
            stopwatch.stop();
            resultTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (resultTime <= elementDelayTime + ACCEPTED_DIFFERENCE && resultTime < timeOutMillis) {
                return true;
            } else {
                logger.info("Method: WaitForNestedElementToBeDisplayed() does not seem to have stopped timeout around the same time as element was displayed.");
                return false;
            }
        } catch (WebDriverException e) {
            logger.info("Element was not in an expected state");
            return false;
        }
    }

    public boolean waitForNestedElementsToBeDisplayed(int timeOutMillis, int elementDelayTime) {
        final long ACCEPTED_DIFFERENCE = 1000;
        long resultTime;
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            elementHelper.waitForNestedElementsToBeDisplayed(parentElement, By.className("child-ul"), timeOutMillis);
            stopwatch.stop();
            resultTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if (resultTime <= elementDelayTime + ACCEPTED_DIFFERENCE && resultTime < timeOutMillis) {
                return true;
            } else {
                logger.info("Method: WaitForNestedElementsToBeDisplayed() does not seem to have stopped timeout around the same time as element was displayed.");
                return false;
            }
        } catch (WebDriverException e) {
            logger.info("Element was never displayed");
            return false;
        }
    }

    public boolean waitForAttributeValueToBePresent(String value, int timeout) {
        try {
            elementHelper.waitForAttributeValueToBePresent(inputElement, "type", value, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean waitForAttributeValueToContain(String value, int timeout) {
        try {
            elementHelper.waitForAttributeValueToContain(inputElement, "type", value, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

}
