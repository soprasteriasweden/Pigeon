package se.soprasteria.automatedtesting.webdriver.web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public abstract class WebBasePageObject extends BasePageObject {

    @FindBy(id = "mainpagelink")
    protected WebElement mainPageLink;
    @FindBy(id = "absolutepagelink")
    protected WebElement absolutePositionPageLink;
    @FindBy(id = "screenshotpagelink")
    protected WebElement screenshotPageLink;
    @FindBy(id = "scrollpagelink")
    protected WebElement scrollPageLink;
    @FindBy(id = "loginlink")
    protected WebElement loginLink;
    @FindBy(id ="loggedinmessage")
    protected WebElement loggedInIndicator;

    public WebBasePageObject(AutomationDriver driver) {
        super(driver);
    }

    public abstract void navigateTo();

    public void logIn() {
        elementHelper.clickWithinTime(loginLink, 3000);
    }

    public boolean loggedIn() {
        return elementHelper.isElementPresentAndDisplayedWithinTime(loggedInIndicator, 500);
    }

}