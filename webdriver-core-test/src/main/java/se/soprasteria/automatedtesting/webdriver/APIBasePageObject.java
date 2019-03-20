package se.soprasteria.automatedtesting.webdriver;

import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public abstract class APIBasePageObject extends BasePageObject {

    public APIBasePageObject(AutomationDriver driver) {
        super(driver);
        if(driver.isWeb()) {
            defaultWebpageElementLocator(driver);
        } else if(driver.isAndroid()) {
            defaultAndroidElementLocator(driver);
        } else if(driver.isIos()) {
            defaultIOSElementLocator(driver);
        }
    }

    @Override
    public boolean isPageLoaded() {
        return false;
    }

}
