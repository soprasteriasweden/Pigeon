package se.soprasteria.automatedtesting.webdriver.web.methods;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;

import java.util.NoSuchElementException;

public class ClickElementMethods extends MainPage {

    public ClickElementMethods(AutomationDriver driver) {
        super(driver);
    }

    public boolean clickShowGoToAnotherPageButton() {
        return elementHelper.clickWithinTime(goToAnotherPageTimerButton,2000);
    }

    public boolean clickTimerShowButton() {
        return elementHelper.clickWithinTime(showElementTimerButton, 2000);
    }

    public boolean clickTimerShowButton(int elementDelayTime) {
        String attributeValue = getOnClickValueString(JS_FUNCTION_SHOW, "delayedelement",elementDelayTime);
        setElementDelayOnClick(showElementTimerButton, attributeValue);
        return elementHelper.clickWithinTime(showElementTimerButton, 2000);
    }

    public boolean clickTimerHideButton() {
            return elementHelper.clickWithinTime(hideElementTimerButton, 2000);
    }

    public boolean clickTimerHideButton(int elementDelayTime) {
        String attributeValue = getOnClickValueString(JS_FUNCTION_HIDE, "delayedhiddenelement",elementDelayTime);
        setElementDelayOnClick(hideElementTimerButton,attributeValue);
        return elementHelper.clickWithinTime(hideElementTimerButton, 2000);
    }

    public boolean clickTimerRemoveButton() {
        return elementHelper.clickWithinTime(removeElementTimerButton, 2000);
    }

    public boolean clickTimerRemoveButton(int elementDelayTime) {
        String attributeValue = getOnClickValueString(JS_FUNCTION_REMOVE,"delayedremovedelement", elementDelayTime);
        setElementDelayOnClick(removeElementTimerButton,attributeValue);
        return elementHelper.clickWithinTime(removeElementTimerButton, elementDelayTime);
    }

    public boolean clickShowTextButton() {
        return elementHelper.clickWithinTime(showTextButton, 1000);
    }

    public boolean clickShowTextButton(int elementDelayTime) {
        String attributeValue = getOnClickValueString(JS_FUNCTION_INSERT_TEXT,"text",elementDelayTime, EXAMPLE_STRING);
        setElementDelayOnClick(showTextButton,attributeValue);
        return elementHelper.clickWithinTime(showTextButton, 1000);
    }

    public boolean clickChangeValueButton() {
        return elementHelper.clickWithinTime(changeValueButton, 2000);
    }

    public boolean clickDisplayAndEnableButton() {
        return elementHelper.clickWithinTime(displayAndEnableElementButton, 2000);
    }

    public boolean clickShowNestedElements() {
        return elementHelper.clickWithinTime(showChildrenButton, 2000);
    }

    public boolean clickShowNestedElements(int elementDelayTime) {
        String attributeValue = getOnClickValueString(JS_FUNCTION_SHOW_BY_CLASS,"child-ul",elementDelayTime);
        setElementDelayOnClick(showChildrenButton,attributeValue);
        return elementHelper.clickWithinTime(showChildrenButton, 1000);
    }

    public boolean clickCreateAndDisplayNewElementButton() {
        return elementHelper.clickWithinTime(createAndDisplayNewElementButton, 2000);
    }

    public boolean clickCreateAndHideNewElementButton() {
        return elementHelper.clickWithinTime(createAndHideNewElementButton, 2000);
    }

    public boolean clickShowElementButton() {
        return elementHelper.clickWithinTime(showClickableElementButton, 1000);
    }

    public boolean clickWithinTime() {
        return elementHelper.clickWithinTime(clickableElement, 1000);
    }

    public boolean clickWithinTimeWithMessage() {
       try {
           elementHelper.clickWithinTime(clickableElement, 1000, "Element was not clickable");
           return true;
       } catch (AssertionError e){
           return false;
       }
    }

    public String getOnClickValueString(String jsFunctionName, String elementID, int delay) {
        return jsFunctionName + "(\"" + elementID + "\"," + delay +");";
    }

    public String getOnClickValueString(String jsFunctionName, String elementID, int delay, String text) {
        return jsFunctionName + "(\"" + elementID + "\"," + delay +",\"" + text +"\");";
    }

    public void setElementDelayOnClick(WebElement webElement, String value) {
        elementHelper.changeElementAttributeValue(webElement, "onclick", value);
    }

}