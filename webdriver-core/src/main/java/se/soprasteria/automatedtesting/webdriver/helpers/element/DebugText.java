package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.WebElement;


public class DebugText {
    public static String getElementDebugText(WebElement webElement) {
        return "Tag: " + webElement.getTagName() +
                        "    isDisplayed: " + String.valueOf(webElement.isDisplayed()) +
                        "    isEnabled: " + String.valueOf(webElement.isEnabled()) +
                        "    Text: " + webElement.getText();

    }
}
