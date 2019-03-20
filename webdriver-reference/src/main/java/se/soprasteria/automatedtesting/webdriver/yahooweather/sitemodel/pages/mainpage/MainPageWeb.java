package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class MainPageWeb extends BasePageObject implements MainPage  {
    @FindBy(xpath = "//button[contains(.,'Change location')]")
    protected WebElement buttonChangeLocation;
    @FindBy(xpath = "//div[contains(@class,'location')]/h1[contains(@class, 'city')]")
    protected WebElement currentLocationHeader;
    @FindBy(id = "uh-signedin")
    protected WebElement signInButton;
    @FindBy(css = "[data-reactid='447']")
    protected WebElement windIndicator;
    @FindBy(css = "div#Lead-1-WeatherLocationAndTemperature > div > section > div:nth-of-type(3) > span")
    protected WebElement currentTime;

    private final int POLL_MILLIS = 500;

    public MainPageWeb(AutomationDriver driver) {
        super(driver);
        defaultWebpageElementLocator(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Verifying that the mainpage is visible.");
        return  currentLocationHeader.isDisplayed();
    }

    public String getCurrentLocation() {
        String currentLocation = driver.getTitle().split(",")[0];
        logger.warn(String.format("Current location: %s", currentLocation));
        return currentLocation;
    }

    public boolean waitForExpectedLocation(String expected, int millisToWait) {
        logger.info("Waiting for expected location to be found (checking page title)");
        logger.info("Expected location: " + expected + "    milliseconds to wait: " + millisToWait);
        int elapsedMillis = 0;
        while (elapsedMillis < millisToWait) {
            String current = getCurrentLocation();
            if (current.contentEquals(expected)) {
                logger.info("Found expected location, returning true");
                return true;
            }
            sleep(POLL_MILLIS);
            elapsedMillis += POLL_MILLIS;
        }

        logger.info("Could not find expected location, it did not appear in the title of the page, returning false");
        logger.info("expected: " + expected + "    current: " + getCurrentLocation());
        return false;
    }

    public void gotoAddLocation() {
        logger.info("Going to add add location by pressing the change location button");
        elementHelper.clickWithinTime(buttonChangeLocation,2000);
    }
    /*   Mobile specific methods  */
    public void swipeDown() {}
    public void swipeUp() {}
    public void swipeLeft() {}
    public void swipeRight() {}

    public boolean windIndicatorChanged(AutomationDriver driver){
        navigationHelper.scrollToElement(windIndicator);
        return elementHelper.didElementChangeDuringInterval(windIndicator, 2000);
    }

    public boolean hasSiteChanged() {
        driver.executeJavaScript("arguments[0].scrollIntoView(true);", windIndicator);
        return driver.didScreenChangeDuringInterval(2000);
    }

    public String  getCurrentTime() {
        String localTime = currentTime.getText();
        int indexOfColon = localTime.indexOf(":");
        return currentTime.getText().substring(indexOfColon - 2, indexOfColon + 3).trim();
    }
}
