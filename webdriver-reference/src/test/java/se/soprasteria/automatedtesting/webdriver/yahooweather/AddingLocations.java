package se.soprasteria.automatedtesting.webdriver.yahooweather;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.yahooweather.framework.base.YahooBaseTest;



public class AddingLocations extends YahooBaseTest {

    final String[] VALID_LOCATIONS = {"London"};
    final String[] INVALID_LOCATIONS = {"Rockholm"};



    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android", "ios", "browser"})
    public void openSearchLocationPageByClickingAddLocationButton(AutomationDriver driver) throws InterruptedException {
        logger.info("Starting test that verifies we can open the page/editbox that adds a location correctly ");
        initPages(driver);
        mainPage.gotoAddLocation();
        Assert.assertTrue(addLocation.isPageLoaded(), "Expected add/search location page to be loaded, it wasn't");
        logger.info("Test passed - add/search location page was successfully opened");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android", "ios", "browser"})
    public void closeSearchLocationPageByClickingClearButton(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        logger.info("Testing that we can open the page which you add a location and verifies that it closes correctly.");
        mainPage.gotoAddLocation();
        Assert.assertTrue(addLocation.isPageLoaded(),
                "Expected add/search location page to be loaded, it wasn't");
        addLocation.clearSearch();
        Assert.assertTrue(mainPage.isPageLoaded(),
                "Expected mainpage to be loaded after closing down search, it wasn't");
        logger.info("Test passed - Mainpage was open as expected after clearing search");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android", "browser"})
    public void addValidLocation(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        logger.info("Testing if we can add correct locations and verify that they get added properly.");
        for (String expectedLocation: VALID_LOCATIONS) {
            mainPage.gotoAddLocation();
            Assert.assertTrue(addLocation.addNewLocation(expectedLocation), "Could not add valid location");
            logger.info("Test done, ready for assertion, this is the info currently:");
            Assert.assertTrue(mainPage.waitForExpectedLocation(expectedLocation, 7000),
                    "Failed to add a location. Expected: " + expectedLocation +
                            "   Actual: " + mainPage.getCurrentLocation());
        }
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android", "browser"})
    public void addInvalidLocations(AutomationDriver driver) {
        initPages(driver);
        logger.info("Testing to add invalid locations and verifies that they cannot be added.");
        for (String location: INVALID_LOCATIONS) {
            mainPage.gotoAddLocation();
            addLocation.addNewLocation(location);
            Assert.assertFalse(mainPage.waitForExpectedLocation(location, 3000),
                    "Successfully added non-existing location which should not be possible. Added location: " + location);
        }
        logger.info("Test passed - Could not add any invalid locations");
    }
}
