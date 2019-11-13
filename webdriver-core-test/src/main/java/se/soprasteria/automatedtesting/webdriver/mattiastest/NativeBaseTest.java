package se.soprasteria.automatedtesting.webdriver.mattiastest;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class NativeBaseTest extends BaseTestCase {
    protected MainPage mainPage;

    @Override
    protected String getDriverConfigId() {
        return "android_Oneplus6_textapp_native";
    }

    @Override
    protected String getConfigFile() {
        return "config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {
        mainPage = new MainPage(driver);
    }


}
