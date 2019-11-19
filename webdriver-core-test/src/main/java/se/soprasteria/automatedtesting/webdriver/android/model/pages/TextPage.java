package se.soprasteria.automatedtesting.webdriver.android.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class TextPage extends BasePageObject {

    @FindBy(id = "textInput")
    WebElement textInput;
    @FindBy(id = "passwordInput")
    WebElement passwordInput;

    public TextPage(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementPresentAndDisplayedWithinTime(textInput, 5000);
    }

    public void enterText(String text) {
        logger.info("Sending \"" + text + "\" to text input");
        elementHelper.sendKeysWithControlledSpeed(textInput, text, 100);
        Assert.assertEquals(textInput.getText(), text);
    }

    public String getText() {
        return textInput.getText();
    }

    public void enterPassword(String password) {
        logger.info("Entering password into password input");
        elementHelper.sendKeysWithControlledSpeed(passwordInput, password, 100);
    }

    public void clearText() {
        textInput.clear();
    }
}
