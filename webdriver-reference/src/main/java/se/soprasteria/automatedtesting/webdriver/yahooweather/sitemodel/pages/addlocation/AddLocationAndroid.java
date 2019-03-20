package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class AddLocationAndroid extends BasePageObject implements AddLocation {

    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/location_search_box")
    protected WebElement searchField;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/close_button")
    protected WebElement searchResetButton;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/location_search_result")
    protected WebElement resultBox;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/location_name")
    protected WebElement resultLocationName;
    @FindBy(id = "android:id/empty")
    protected WebElement noResultsFound;

    protected By resultItem = By.id("com.yahoo.mobile.client.android.weather:id/location_item_holder");


    public AddLocationAndroid(AutomationDriver driver) {
        super(driver);
        defaultAndroidElementLocator(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Checking to see if the add location page is loaded by verifying if the search field is displayed");
        return elementHelper.isElementDisplayedWithinTime(searchField, 3000);
    }

    /*   Actions   */
    public void clearSearch() {
        logger.info("Clearing search by clicking search reset button");
        searchResetButton.click();
    }

    public boolean addNewLocation(String location) {
        logger.info("Adding new location by entering text into searchfield, location: " + location);
        elementHelper.waitForElementToBePresentAndDisplayed(searchField, 2000);
        searchField.sendKeys(location);
        if (elementHelper.isElementDisplayedWithinTime(noResultsFound, 2000)) {
            logger.info("No results were found, navigating out of search page by pressing back twice and returning false");
            driver.navigate().back();
            driver.navigate().back();
            return false;
        }
        logger.info("Found some results, sleeping for 2 seconds then choose the first result no matter what the text and returning true");
        sleep(1000);
        resultBox.findElements(resultItem).get(0).click();

        return true;
    }
}
