package se.soprasteria.automatedtesting.webdriver.yahooweather;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.yahooweather.framework.base.YahooBaseTest;


public class SwitchBetweenLocations extends YahooBaseTest {

    final String[] VALID_LOCATIONS = {"London", "Stockholm"};
    final int counter = 2;

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android"})
    public void switchValidLocation(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        logger.info("Testing if we can switch between 2 added locations by swiping and verify that they get added properly.");
        //verify adding first location
        for (String expectedLocation1: VALID_LOCATIONS) {
            mainPage.gotoAddLocation();
            addLocation.addNewLocation(expectedLocation1);
            logger.info("Test done, ready for assertion, this is the info currently:");
            Assert.assertTrue(mainPage.waitForExpectedLocation(expectedLocation1, 7000),
                    "Failed to add a location. Expected: " + expectedLocation1 +
                            "   Actual: " + mainPage.getCurrentLocation());
        }

        //verify switching between the 2 added locations
	    int i=0;
		while (i < counter) {
			mainPage.swipeRight();
			Assert.assertTrue (mainPage.waitForExpectedLocation (String.valueOf (VALID_LOCATIONS[0]), 7000),
					"Failed to switch between locations. Expected: " + String.valueOf (VALID_LOCATIONS[0]));
			mainPage.swipeUp();
			Assert.assertTrue (mainPage.isPageLoaded (), "The page is not loaded yet");
			mainPage.swipeDown();
			mainPage.swipeLeft();
			Assert.assertTrue (mainPage.waitForExpectedLocation (String.valueOf (VALID_LOCATIONS[1]), 7000),
					"Failed to switch between locations. Expected: " + String.valueOf (VALID_LOCATIONS[1]));
			mainPage.swipeUp();
			Assert.assertTrue (mainPage.isPageLoaded (), "The page is not loaded yet");
			mainPage.swipeDown();
			i++;
		}
    }

}
