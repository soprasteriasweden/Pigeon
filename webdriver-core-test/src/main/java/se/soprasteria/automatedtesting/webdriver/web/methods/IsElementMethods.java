package se.soprasteria.automatedtesting.webdriver.web.methods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.datastructure.ElementState;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;

import static se.soprasteria.automatedtesting.webdriver.web.datastructure.ElementState.ELEMENT_WILL_BE_DISPLAYED;

public class IsElementMethods extends MainPage {

    public IsElementMethods(AutomationDriver driver) {
        super(driver);
    }

    public boolean isElementDisplayedAndRefreshedWithinTime(int timeOutMillis) {
        return elementHelper.isElementDisplayedAndRefreshedWithinTime(hiddenDisabledButton,timeOutMillis);
    }

    public boolean isElementClickableAndRefreshedWithinTime(int timeOutMillis) {
        return elementHelper.isElementClickableAndRefreshedWithinTime(hiddenDisabledButton,timeOutMillis);
    }

    public boolean isNestedElementDisplayedWithinTime(int timeOutMillis) {
        return elementHelper.isNestedElementDisplayedWithinTime(parentElement, By.className("child-ul"),timeOutMillis);
    }

    public boolean isNestedElementsDisplayedWithinTime(int timeOutMillis) {
        return elementHelper.isNestedElementDisplayedWithinTime(parentElement,By.className("child-ul"),timeOutMillis);
    }

    public boolean isAttributeValuePresentWithinTime(String value) {
        return elementHelper.isAttributeValuePresentWithinTime(inputElement,"type",value,2000);
    }

    public boolean isClickableElementDisplayed() {
        return elementHelper.isElementDisplayedWithinTime(clickableElement, 1000);
    }

    public boolean isElementHiddenWithinTime(int timeOutMillis) {
        return elementHelper.isElementHiddenWithinTime(delayedHiddenElement, timeOutMillis);
    }

    public boolean isElementGoneWithinTime(int timeOutMillis) {
        return elementHelper.isElementGoneWithinTime(delayedRemovedElement, timeOutMillis);
    }

    public boolean isElementPresentAndDisplayed(boolean isElementPresent, boolean isElementDisplayed) {
        WebElement element;
        if (isElementDisplayed) {
            element = displayedElement;
        } else if (isElementPresent) {
            element = hiddenElement;
        } else {
            element = nonExistingElement;
        }
        return elementHelper.isElementPresentAndDisplayed(element);
    }

    public boolean isElementPresentAndDisplayedWithinTime(ElementState state, int timeOutMillis) {
        if (state == ELEMENT_WILL_BE_DISPLAYED) {
            return elementHelper.isElementPresentAndDisplayedWithinTime(createdAndDisplayedElement, timeOutMillis);
        } else {
            return elementHelper.isElementPresentAndDisplayedWithinTime(createdAndHiddenElement, timeOutMillis);
        }
    }

    public boolean isElementPresentWithinTime(int timeOutMillis) {
        elementHelper.clickWithinTime(createAndHideNewElementButton, 2000);
        return elementHelper.isElementPresentWithinTime(By.id("createdhiddenbutton"), timeOutMillis);
    }

    public boolean isTextPresentInElementWithinTime() {
        return elementHelper.isTextPresentInElementWithinTime(text, EXAMPLE_STRING, 2000);
    }

    public boolean isElementHiddenOrGoneWithinTimeWithElement(int timeOutMillis, boolean elementIsRemoved) {
        if (elementIsRemoved) {
            return elementHelper.isElementHiddenOrGoneWithinTime(delayedRemovedElement, timeOutMillis);
        }
        return elementHelper.isElementHiddenOrGoneWithinTime(delayedHiddenElement, timeOutMillis);
    }

    public boolean isElementHiddenOrGoneWithinTime(int timeOutMillis, boolean isElementPresent) {
        if (isElementPresent) {
            return elementHelper.isElementHiddenOrGoneWithinTime(By.id("delayedremovedelement"), timeOutMillis);
        }
        return elementHelper.isElementHiddenOrGoneWithinTime(By.id("delayedhiddenelement"), timeOutMillis);
    }

    public boolean isElementClickableWithinTime(int timeOutMillis) {
        return elementHelper.isElementClickableWithinTime(delayedElement, timeOutMillis);
    }

    public boolean isOneTabOpened() {
        return driver.getWindowHandles().size() == 1;
    }

    public boolean isElementDisplayedWithinTime(int timeOutMillis) {
        return elementHelper.isElementDisplayedWithinTime(delayedElement, timeOutMillis);
    }

    public boolean isGoogleLogoPresent() {
        try {
            return elementHelper.isElementPresentWithinTime(googleLogoBy, 5000);
        } catch (WebDriverException e) {
            logger.info("Element is not displayed on the page");
            return false;
        }
    }

    public boolean isDisplayedElementsListCorrect() {
        int size = elementHelper.getDisplayedElements(listOfElements).size();
        if(size != NUMBER_OF_DISPLAYED_ELEMENTS) {
            logger.trace("Wrong amount of elements displayed. " + size + " elements displayed, should have been + " + NUMBER_OF_DISPLAYED_ELEMENTS);
            return false;
        }
        return true;
    }

    public boolean isHiddenElementsListCorrect() {
        int size = elementHelper.getHiddenElements(listOfElements).size();
        if(size != NUMBER_OF_HIDDEN_ELEMENTS) {
            logger.trace("Wrong amount of elements hidden. " + size + " elements hidden, should have been + " + NUMBER_OF_HIDDEN_ELEMENTS);
        }
        return true;
    }

}
