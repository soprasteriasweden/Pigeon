package se.soprasteria.automatedtesting.webdriver.web;

import org.testng.Assert;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.datastructure.Page;
import se.soprasteria.automatedtesting.webdriver.web.methods.*;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.AbsoluteElementPage;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.ElementScreenshotPage;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.ScrollPage;

import java.awt.*;

public class WebBaseTestCase extends BaseTestCase {

    protected DriverMethods driverMethods;
    protected WaitForElementMethods waitForElementMethods;
    protected IsElementMethods isElementMethods;
    protected ClickElementMethods clickElementMethods;
    protected ElementMethods elementMethods;

    protected MainPage mainPage;
    protected AbsoluteElementPage absoluteElementPage;
    protected ScrollPage scrollPage;
    protected ElementScreenshotPage elementScreenshotPage;


    @Override
    protected String getDebugLevel() {
        return "IMAGES_FAIL";
    }

    @Override
    protected String getDriverConfigId() {
        return "chromedriver";
    }

    @Override
    protected String getConfigFile() {
        return "config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {
        logger.info("Initializing pages");
        mainPage = new MainPage(driver);
        absoluteElementPage = new AbsoluteElementPage(driver);
        scrollPage = new ScrollPage(driver);
        elementScreenshotPage = new ElementScreenshotPage(driver);
        initTestMethods(driver);

    }

    private void initTestMethods(AutomationDriver driver) {
        driverMethods = new DriverMethods(driver);
        waitForElementMethods = new WaitForElementMethods(driver);
        isElementMethods = new IsElementMethods(driver);
        clickElementMethods = new ClickElementMethods(driver);
        elementMethods = new ElementMethods(driver);
    }

    protected void obviateMouse() {
        if (driver.isWeb()) {
            try {
                new Robot().mouseMove(0, 0);
            } catch (AWTException e) {
                logger.error("Could not move mouse to top left corner");
            }
        }
    }

    public void initialize(Page page) {
        switch (page) {
            case MAIN_PAGE:
                mainPage.initWeb(driver);
                Assert.assertTrue(mainPage.isLoaded(), "Page did not load correctly");
                break;
            case SCROLL_PAGE:
                scrollPage.initWeb(driver);
                Assert.assertTrue(scrollPage.isLoaded(), "Page did not load correctly");
                break;
            case ABSOLUTE_ELEMENT_PAGE:
                absoluteElementPage.initWeb(driver);
                Assert.assertTrue(absoluteElementPage.isLoaded(), "Page did not load correctly");
                break;
            case ELEMENT_SCREENSHOT_PAGE:
                elementScreenshotPage.initWeb(driver);
                Assert.assertTrue(elementScreenshotPage.isLoaded(), "Page did not load correctly");
                break;
        }
    }
}
