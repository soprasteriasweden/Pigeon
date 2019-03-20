package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.menu;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class SidemenuAndroid extends BasePageObject implements Sidemenu {

    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/sidebar_logo")
    protected WebElement menuLogo;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/sidebar_item_settings")
    protected WebElement menuItemSettings;


    public SidemenuAndroid(AutomationDriver driver) {
        super(driver);
        defaultAndroidElementLocator(driver);
    }

    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(menuLogo, 1000);
    }


}
