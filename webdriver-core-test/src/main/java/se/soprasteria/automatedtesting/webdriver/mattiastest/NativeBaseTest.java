package se.soprasteria.automatedtesting.webdriver.mattiastest;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class NativeBaseTest extends BaseTestCase {
    protected MainPage mainPage;

    @Override
    protected String getDriverConfigId() {
        return "android_pixel3_textapp";
    }

    @Override
    protected String getConfigFile() {
        return "config_test.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {
        mainPage = new MainPage(driver);
    }


}
