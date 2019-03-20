package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.dailynotifications;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

/**
 *
 * Description: The first page that pops up when starting the app, asking the user to enable daily notifications. Very
 * simple page and only pops up on first time startup (note: this always happens when running appium sessions in full
 * reset mode.
 *
 */
public class DailyNotificationsAndroid extends BasePageObject implements DailyNotifications {

    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/onboarding_title_text")
    protected WebElement pageHeader;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/onboarding_notifications_positive_button")
    protected WebElement enableNotifications;
    @FindBy(id = "com.yahoo.mobile.client.android.weather:id/onboarding_notifications_negative_button")
    protected WebElement disableNotifications;

    public DailyNotificationsAndroid(AutomationDriver driver) {
        super(driver);
        defaultAndroidElementLocator(driver);
    }

    public void enableNotifications() {
        enableNotifications.click();
    }

    public void disableNotifications() {
        disableNotifications.click();
    }

    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(pageHeader, -1);
    }

    public void acceptAndroidPermissionPopups() { super.acceptAndroidPermissionPopups(); }
}
