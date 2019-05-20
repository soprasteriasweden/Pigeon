package se.soprasteria.automatedtesting.webdriver.api.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import se.soprasteria.automatedtesting.webdriver.api.utility.ElementHelper;
import se.soprasteria.automatedtesting.webdriver.api.utility.NavigationHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.time.Duration;

/**
 *
 * Base pageobject to extend when creating pageobjects. Initializes a logger and ensures the existence of the
 * isPageLoaded() state and action. Also houses elementHelper that has functions to streamline working with
 * selenium webelements and driverhelpers that streamline working with selenium webdrivers.
 *
 * Page objects is a designpattern used by us to basically wrap all the technical functionality to interact with the
 * page behind functions that are easier to understand.
 *
 */
@SuppressWarnings("unused")
public abstract class BasePageObject extends BaseClass {
    /** The webdriver that drives operations (actions/checks) on the page */
    protected final AutomationDriver driver;
    /** The ElementHelper class has functions to make working with Selenium WebElements easier */
    protected final ElementHelper elementHelper;
    /** The NavigationHelper class has functions to make navigation within browser or appwindow easier**/
    protected final NavigationHelper navigationHelper;

    public BasePageObject(AutomationDriver driver) {
        this.driver = driver;
        this.elementHelper = new ElementHelper(logger, driver);
        this.navigationHelper = new NavigationHelper(logger,driver);
        this.setElementLocator(driver);
    }

    public void setElementLocator(AutomationDriver driver){
        if(driver.isWeb()) {
            defaultWebpageElementLocator(driver);
        } else if(driver.isAndroid()) {
            defaultAndroidElementLocator(driver);
        } else if(driver.isIos()) {
            defaultIOSElementLocator(driver);
        }
    }

    /**
     * The windows application elementlocator has to be called within the pageobject (read about pagefactories for more info) before
     * interacting with any of the elements on the page.
     *
     * @param driver
     */
    protected void defaultWindowsDriverElementLocator(AutomationDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver.getWindowsDriver(), Duration.ZERO), this);
    }

    /**
     * The webdriver elementlocator has to be called within the pageobject (read about pagefactories for more info) before
     * interacting with any of the elements on the page.
     *
     * @param driver
     */
    protected void defaultWebpageElementLocator(AutomationDriver driver) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver.getWebDriver(), 0), this);
    }

    /**
     * The webdriver elementlocator has to be called within the pageobject (read about pagefactories for more info) before
     * interacting with any of the elements on the page.
     *
     * @param driver
     */
    protected void defaultAndroidElementLocator(AutomationDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver.getAndroidDriver(), Duration.ZERO), this);
    }

    /**
     * The webdriver elementlocator has to be called within the pageobject (read about pagefactories for more info) before
     * interacting with any of the elements on the page.
     *
     * @param driver
     */
    protected void defaultIOSElementLocator(AutomationDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver.getIOSDriver(), Duration.ZERO), this);
    }

    /**
     * Get the webdriver used in this pageobject.
     *
     * @return webdriver
     */
    public AutomationDriver getDriver() {
        return driver;
    }

    /**
     * Abstract function that should always be implemented when creating a new pageobject.
     *
     * @return Specifies whether or not the page is loaded currently.
     */
    public abstract boolean isPageLoaded();

    public void acceptAndroidPermissionPopups(){
        waitForAndroidPermissionPopupAndClickPast("com.android.packageinstaller:id/permission_allow_button", "allow");
    }

    public void denyAndroidPermissionPopups(){
        waitForAndroidPermissionPopupAndClickPast("com.android.packageinstaller:id/permission_deny_button", "deny");
    }

    private void waitForAndroidPermissionPopupAndClickPast(String buttonId, String buttonName) {
        if(elementHelper.isElementPresentWithinTime(By.id("com.android.packageinstaller:id/permission_message"), 3000)){
            WebElement popup = driver.findElement(By.id("com.android.packageinstaller:id/permission_message"));
            logger.debug("Android permission popup found with message: " + popup.getText());
            elementHelper.clickWithinTime(driver.findElement(By.id(buttonId)), 3000);
            logger.debug("Clicking " + buttonName + " button");
            elementHelper.waitForElementToBeGone(popup, 1000);
            waitForAndroidPermissionPopupAndClickPast(buttonId, buttonName);
        }
    }

}
