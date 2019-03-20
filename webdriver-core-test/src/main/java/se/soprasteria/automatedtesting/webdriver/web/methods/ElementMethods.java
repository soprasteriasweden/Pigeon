package se.soprasteria.automatedtesting.webdriver.web.methods;

import com.google.common.base.Stopwatch;
import org.openqa.selenium.WebDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class ElementMethods extends MainPage {

    public ElementMethods(AutomationDriver driver) {
        super(driver);
    }

    public boolean openLinkInSameTab() {
        try {
            elementHelper.isElementClickableWithinTime(goToAnotherPageButton, 2000);
        } catch (NoSuchElementException e) {
            logger.info("Element was not clickable within 2000ms");
            return false;
        }
        return elementHelper.openElementLinkInSameTab(goToAnotherPageButton);
    }

    public boolean openLinkInSameTabWithinTime(int timeOutMillis) {
        return elementHelper.openElementLinkInSameTab(hiddenGoToPageButton, timeOutMillis);
    }

    public boolean sendKeysWithControlledSpeed() {
        final long ACCEPTED_EXECUTION_TIME = 60000;
        String textToSend = EXAMPLE_STRING;
        int millisBetweenKeyPress = 100;
        Stopwatch stopwatch = Stopwatch.createStarted();
        elementHelper.sendKeysWithControlledSpeed(textInput, textToSend, millisBetweenKeyPress);
        long executionTotalMillis = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        long individualKeyPressExecutionTime = (executionTotalMillis - (millisBetweenKeyPress * textToSend.length())) / textToSend.length();
        logger.info("INDIVIDUAL KEY PRESS EXECUTION TIME: " + individualKeyPressExecutionTime);
        String textFromInputField = textInput.getAttribute("value");

        if(textFromInputField.equalsIgnoreCase(textToSend)
                && executionTotalMillis < ACCEPTED_EXECUTION_TIME){
            return true;
        }
        return false;
    }
    public boolean doesAttributeValueContainWithinTime(String value) {
        return elementHelper.doesAttributeValueContainWithinTime(inputElement,"type",value,2000);
    }

}
