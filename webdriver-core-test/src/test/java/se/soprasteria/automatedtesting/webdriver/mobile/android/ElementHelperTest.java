package se.soprasteria.automatedtesting.webdriver.mobile.android;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.android.AndroidBaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class ElementHelperTest extends AndroidBaseTestCase {

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    protected void buttonInteractionTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        Assert.assertTrue(elementPage.showElement(), "Element did not show");
        Assert.assertTrue(elementPage.showElementDelay(), "Element did not show after delay");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    protected void hideElementTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        Assert.assertTrue(elementPage.hideElement(), "Element was not hidden");
        Assert.assertTrue(elementPage.hideElementDelay(), "Element was not hidden after delay");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    protected void checkBoxTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        Assert.assertTrue(elementPage.clickCheckBox(), "Check box was not selected correctly");
    }

}
