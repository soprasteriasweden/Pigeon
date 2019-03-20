package se.soprasteria.automatedtesting.webdriver.yahooweather.framework.base;

import org.openqa.selenium.By;
import org.testng.Assert;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.api.utility.ElementHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.YahooPageFactory;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation.AddLocation;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addpopularplaces.AddPopularPlaces;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.dailynotifications.DailyNotifications;
import se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage.MainPage;


public class YahooBaseTest extends BaseTestCase {

    protected AddLocation addLocation;
    protected MainPage mainPage;

    protected void initPages(AutomationDriver driver) {
        logger.info("Initialising pages to be used in test");
        addLocation = YahooPageFactory.getAddLocation(driver);
        mainPage = YahooPageFactory.getMainPage(driver);
    }

    @Override
    protected String getDefaultDriverConfig() {
        return "android";
    }

    @Override
    protected String getDefaultPropertyFile() {
        return "yahoo/yahoo_config.xml";
    }

    @Override
    protected void initializeDriver(AutomationDriver driver) {
        if (driver.isAndroid()) {
            initYahooAndroid(driver);
        }else if (driver.isWeb()){
            initYahooWeb(driver);
        } else if (driver.isIos()){
            initYahooWebIos(driver);
        }
    }

    private void initYahooAndroid(AutomationDriver driver) {
        logger.info("Initializing the test by closing down permission popups and daily notifications");
        DailyNotifications dailyNotifications = YahooPageFactory.getDailyNotifications(driver);
        dailyNotifications.acceptAndroidPermissionPopups();

        if (dailyNotifications.isPageLoaded()) {
            logger.info("The dailynotifications page is loaded, disabling notifications now");
            dailyNotifications.disableNotifications();
            sleep(1000);
            Assert.assertFalse(dailyNotifications.isPageLoaded(),
                    "Dailynotifications was still loaded when it should'nt have been, could not initialise driver");
        }


        AddPopularPlaces addPopularPlaces = YahooPageFactory.getAddPopularPlaces(driver);

        if (addPopularPlaces.isPageLoaded()) {
            logger.info("The add popular places page is loaded, skipping past it");
            addPopularPlaces.continueToMainProgram();
            MainPage mainPage = YahooPageFactory.getMainPage(driver);
            Assert.assertTrue(mainPage.isPageLoaded(),
                    "The main page was not loaded when it should have been, could not initialise driver");
        }
        logger.info("We should now have passed the welcome sequence and be inside the app");
    }

    private void initYahooWeb(AutomationDriver driver){
        driver.manage().window().maximize();
        driver.navigate().to("https://www.yahoo.com/news/weather/");
        pressAgreeGDPRButton(driver);
        logger.info("We should now be navigating to the yahoo webpage");
    }

    private void initYahooWebIos(AutomationDriver driver){
        driver.navigate().to("https://www.yahoo.com/news/weather/");
        pressAgreeGDPRButton(driver);
        logger.info("We should now have navigating to the yahoo webpage, sleeping for 5s to allow it to initialise");
        sleep(5000);
    }

    private void pressAgreeGDPRButton(AutomationDriver driver) {
        ElementHelper elementHelper = new ElementHelper(logger, driver);
        By agreeButton = By.name("agree");
        if (elementHelper.isElementDisplayedWithinTime(agreeButton, 2500)) {
            elementHelper.clickWithinTime(driver.findElement(agreeButton), 2000);
        } else {
            logger.debug("No GDPR agree button found, it may have been removed from the site.");
        }
    }

}
