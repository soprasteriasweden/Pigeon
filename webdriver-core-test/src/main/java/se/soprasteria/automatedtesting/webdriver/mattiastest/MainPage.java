package se.soprasteria.automatedtesting.webdriver.mattiastest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.io.IOException;

public class MainPage extends BasePageObject {

    @FindBy(id = "textInput")
    protected WebElement textInput;
    @FindBy(id = "passwordInput")
    protected WebElement passwordInput;


    public MainPage(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(textInput, 10000);
    }

    public void enterText(String text) {
        elementHelper.sendKeysWithControlledSpeed(textInput, text, 100);
    }

    public String getText(){
        return textInput.getText();
    }

    public void clearText(){
        if(elementHelper.isElementPresentAndDisplayedWithinTime(textInput, 5000))
            textInput.clear();
    }

    public void enterPassword(String password) {
        elementHelper.sendKeysWithControlledSpeed(passwordInput, password, 400);
    }


}
