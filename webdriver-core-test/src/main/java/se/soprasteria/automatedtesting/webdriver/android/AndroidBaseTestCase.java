package se.soprasteria.automatedtesting.webdriver.android;

import org.openqa.selenium.WebDriver;
import se.soprasteria.automatedtesting.webdriver.android.model.pages.ButtonPageObject;
import se.soprasteria.automatedtesting.webdriver.android.model.pages.SwipePageObject;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class AndroidBaseTestCase extends BaseTestCase {

    protected ButtonPageObject buttonPageObject;
    protected SwipePageObject swipePage;

    @Override
    protected String getDriverConfigId() {
        return "android";
    }

    @Override
    protected String getConfigFile() {
        return "config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {

    }

    @Override
    protected void initializeDriver(AutomationDriver driver) {
        if (driver.isAndroid()) {
            buttonPageObject = new ButtonPageObject(driver);
            swipePage = new SwipePageObject(driver);
            if(!buttonPageObject.isPageLoaded()) throw new RuntimeException("Page not loaded");
        }
    }

}
