package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addpopularplaces;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

/**
 *
 * Description: The second screen that pops up during the first time setup, where you can add popular places to
 * your feed. This pops up after daily notifications and before finally entering the main screen of the program.
 *
 */
public class AddPopularPlacesAndroid extends BasePageObject implements AddPopularPlaces {

    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/onboarding_location_setup_title_text")
    protected WebElement pageHeader;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/onboarding_location_setup_positive_button")
    protected WebElement continueToMainProgram;


    public AddPopularPlacesAndroid(AutomationDriver driver) {
        super(driver);
        defaultAndroidElementLocator(driver);
    }

    public void continueToMainProgram() {
        continueToMainProgram.click();
    }
    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(pageHeader, 2000);
    }


}
