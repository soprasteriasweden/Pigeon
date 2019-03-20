package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel;

import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation.AddLocation;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation.AddLocationAndroid;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation.AddLocationIOS;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation.AddLocationWeb;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addpopularplaces.AddPopularPlaces;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addpopularplaces.AddPopularPlacesAndroid;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.dailynotifications.DailyNotifications;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.dailynotifications.DailyNotificationsAndroid;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage.MainPage;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage.MainPageAndroid;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage.MainPageWeb;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage.MainPageIOS;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.menu.Sidemenu;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.menu.SidemenuAndroid;


public class YahooPageFactory {

    public static AddLocation getAddLocation(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return new AddLocationAndroid(driver);
        }
        if (driver.isWeb()) {
            return new AddLocationWeb(driver);
        }
        if (driver.isIos()) {
            return new AddLocationIOS (driver);
        }
        throw new RuntimeException(getInvalidDriverError(driver));
    }

    public static AddPopularPlaces getAddPopularPlaces(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return new AddPopularPlacesAndroid(driver);
        }
        throw new RuntimeException(getInvalidDriverError(driver));
    }

    public static DailyNotifications getDailyNotifications(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return new DailyNotificationsAndroid(driver);
        }
        throw new RuntimeException(getInvalidDriverError(driver));
    }

    public static MainPage getMainPage(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return new MainPageAndroid(driver);
        }
        if (driver.isWeb()) {
            return new MainPageWeb(driver);
        }
        if (driver.isIos()) {
            return new MainPageIOS (driver);
        }
        throw new RuntimeException(getInvalidDriverError(driver));
    }

    public static Sidemenu getSidemenu(AutomationDriver driver) {
        if (driver.isAndroid()) {
            return new SidemenuAndroid(driver);
        }
        throw new RuntimeException(getInvalidDriverError(driver));
    }

    private static String getInvalidDriverError(AutomationDriver driver) {
        String[] fixes = {"Change webdriver configuration to a valid one for this project",
                "Implement support for webdriver: " + driver.getWebDriverName()};
        return Errors.getErrorMessage("Tried to start unsupported webdriver", fixes);
    }
}
