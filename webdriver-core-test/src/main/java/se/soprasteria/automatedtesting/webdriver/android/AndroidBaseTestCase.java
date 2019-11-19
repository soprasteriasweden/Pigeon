package se.soprasteria.automatedtesting.webdriver.android;

import se.soprasteria.automatedtesting.webdriver.android.model.BottomNavigation;
import se.soprasteria.automatedtesting.webdriver.android.model.pages.ElementPage;
import se.soprasteria.automatedtesting.webdriver.android.model.pages.SwipePage;
import se.soprasteria.automatedtesting.webdriver.android.model.pages.TextPage;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class AndroidBaseTestCase extends BaseTestCase {
    protected ElementPage elementPage;
    protected SwipePage swipePage;
    protected TextPage textPage;
    protected BottomNavigation bottomNavigation;

    @Override
    protected String getDriverConfigId() {
        return "android_Pixel3_emulator";
    }

    @Override
    protected String getConfigFile() {
        return "config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {
        if(driver.isAndroid()) {
            elementPage = new ElementPage(driver);
            swipePage = new SwipePage(driver);
            textPage = new TextPage(driver);
            bottomNavigation = new BottomNavigation(driver);
        }
    }
}
