package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import static se.soprasteria.automatedtesting.webdriver.api.datastructures.Direction.*;
import static se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed.FAST;

/**
 *
 * Description: The first page that pops up when starting the app, asking the user to enable daily notifications. Very
 * simple page and only pops up on first time startup (note: this always happens when running appium sessions in full
 * reset mode.
 *
 */
public class MainPageAndroid extends BasePageObject implements MainPage {

    /*   Main elements on page   */
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/sidebarButton")
    protected WebElement openSideMenu;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/addLocationButton")
    protected WebElement addLocation;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/location")
    protected WebElement curLocationText;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/local_time")
    protected WebElement curLocationTime;


    public MainPageAndroid(AutomationDriver driver) {
        super(driver);
        defaultAndroidElementLocator(driver);
    }

    /*   States   */
    public boolean isPageLoaded() {
        boolean isCurrentLocationDisplayed = elementHelper.isElementDisplayedWithinTime(curLocationText, 15000);
        if (isCurrentLocationDisplayed) {
            logger.info("The main page is loaded, " +
                    "verified it by seeing that the current location is displayed, returning true");
            return true;
        }
        logger.info("The main page is not loaded, current location header is not displayed, returning false");
        return false;
    }

    public String getCurrentLocation() {
        String currentLocation = curLocationText.getText();
        logger.info("Returning current location based on the text in the header, current location: " + currentLocation);
        return currentLocation;
    }

    public boolean waitForExpectedLocation(String expected, int millisToWait) {
        logger.info("Waiting for the expected location by searching for it in the current location header");
        logger.info("Expected location to search for: " + expected + "     milliseconds to wait: " + millisToWait);
        boolean foundCurrentLocation =
                elementHelper.isTextPresentInElementWithinTime(curLocationText, expected, millisToWait);
        if (foundCurrentLocation) {
            logger.info("The expected location was found in the location header, returning true");
            return true;
        }
        logger.info("We could not find the expected location, returning false. " +
                "Actual value found: "+ curLocationText.getText());
        return false;
    }

    public String getCurrentTime() {
        return curLocationTime.getText().substring(0,5).trim();
    }

    /*   Actions   */
    public void gotoAddLocation() {
        logger.info("Going to add location page by clicking the add location button");
        elementHelper.clickWithinTime(addLocation, 2000);
    }



    /*   Helper functions   */
    public void swipeDown() {
        driver.swipe(DOWN, 50, 50, FAST);
    }

    public void swipeUp() {
        driver.swipe(UP, 50, 50, FAST);
    }

    public void swipeLeft() {
        driver.swipe(LEFT, 50, 80, FAST);
    }

    public void swipeRight() {
        driver.swipe(RIGHT, 50, 80, FAST);
    }


    /*   Web specific methods  */
    public boolean windIndicatorChanged(AutomationDriver driver) { return false; }
    public boolean hasSiteChanged() { return false; }

}
