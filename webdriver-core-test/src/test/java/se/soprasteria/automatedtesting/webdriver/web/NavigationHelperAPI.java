package se.soprasteria.automatedtesting.webdriver.web;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import static se.soprasteria.automatedtesting.webdriver.web.datastructure.Page.SCROLL_PAGE;

public class NavigationHelperAPI extends WebBaseTestCase{

   @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser", "mobile"})
    public void scrollToElement(AutomationDriver driver) throws InterruptedException {
       initialize(SCROLL_PAGE);
       scrollPage.scrollToElement();
       Assert.assertTrue(scrollPage.isElementVisibleWithinViewport(), "Element should be visible in the viewport");
       sleep(1000);
       scrollPage.scrollToTop();
       Assert.assertFalse(scrollPage.isElementVisibleWithinViewport(),"Element should not be visible in the viewport");
       sleep(1000);
    }

}
