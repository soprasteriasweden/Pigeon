package se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.loginpage;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.User;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.loginpage.LoginPage;

public class LoginPageWeb extends BasePageObject implements LoginPage {

    @FindBy(css = ".submit.EdgeButton.EdgeButton--primary.EdgeButtom--medium") // used from classname: submit EdgeButton EdgeButton--primary EdgeButtom--medium
    protected WebElement loginButton;
    @FindBy(css = ".js-username-field.email-input.js-initial-focus") // used from classname: js-username-field email-input js-initial-focus
    protected WebElement usernameTextbox;
    @FindBy(css = ".js-password-field") // used from classname: js-password-field
    protected WebElement passwordTextbox;


    public LoginPageWeb (AutomationDriver driver) {
        super(driver);
        defaultWebpageElementLocator(driver);
    }

    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying that the loginpage is visible");
        return elementHelper.isElementDisplayedWithinTime(loginButton, 5000);
    }

    public void performLogin(User user){
        elementHelper.sendKeysWithControlledSpeed(usernameTextbox, user.username, 0);
        elementHelper.sendKeysWithControlledSpeed(passwordTextbox, user.password, 0);
        elementHelper.clickWithinTime(loginButton, 3000); // will click as soon as it can on this element, and only return false if it couldnt within the timeframe
    }

}
