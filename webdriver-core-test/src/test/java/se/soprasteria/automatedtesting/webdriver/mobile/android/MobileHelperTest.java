package se.soprasteria.automatedtesting.webdriver.mobile.android;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.android.AndroidBaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class MobileHelperTest extends AndroidBaseTestCase {

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void swipeTest(AutomationDriver driver) {
        buttonPageObject.switchToSwipeScreen();
        swipePage.swipeUp();
        Assert.assertTrue(swipePage.isSwipeUpDisplayed());
        swipePage.swipeDown();
        Assert.assertFalse(swipePage.isSwipeDownDisplayed());
        swipePage.swipeLeft();
        Assert.assertTrue(swipePage.isSwipeLeftDisplayed());
        swipePage.swipeRight();
        Assert.assertTrue(swipePage.isSwipeRightDisplayed());
    }

}
