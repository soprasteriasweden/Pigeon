package se.soprasteria.automatedtesting.webdriver.mobile.android;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.android.AndroidBaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class SwipeHelperTest extends AndroidBaseTestCase {

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void swipeTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.navigateSwipePage();
        Assert.assertTrue(swipePage.isPageLoaded(), "Swipe page failed to load correctly");

        Assert.assertTrue(swipePage.swipeUp(), "Failed to swipe up");
        Assert.assertTrue(swipePage.swipeDown(), "Failed to swipe down");
        Assert.assertTrue(swipePage.swipeRight(), "Failed to swipe right");
        Assert.assertTrue(swipePage.swipeLeft(), "Failed to swipe left");
    }

}
