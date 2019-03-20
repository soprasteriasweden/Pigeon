package se.soprasteria.automatedtesting.webdriver.yahooweather;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.WebDriverLog;
import se.soprasteria.automatedtesting.webdriver.yahooweather.framework.base.YahooBaseTest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class AssertContent extends YahooBaseTest {

    String[] locationsSortedByAscendingTimezone = {"London", "Stockholm", "Helsinki"};

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "browser", "android"})
    public void timezonesAreCorrect(AutomationDriver driver) {
        initPages(driver);
        for (String location: locationsSortedByAscendingTimezone) {
            mainPage.gotoAddLocation();
            addLocation.addNewLocation(location);
            Assert.assertEquals(mainPage.getCurrentLocation(), location, "Failed to add location");
            String localTime = mainPage.getCurrentTime();
            String localTimeVerification = getLocalTimeFromEuropeanCity(location);
            Assert.assertEquals(localTime, localTimeVerification, "Local time for location in app not equal with local time verification.");
        }
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "chrome"})
    public void logEntriesExist(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        mainPage.gotoAddLocation();
        Assert.assertTrue(addLocation.isPageLoaded(), "Add location page wasn't loaded when we expected it!");
        addLocation.addNewLocation(locationsSortedByAscendingTimezone[0]);

        // getting logs only works for Chrome and Android currently
        if (driver.isAndroid() || driver.isChrome()) {
            sleep(4000); // give some time to gather entries
            LogEntries entries = WebDriverLog.getLogEntries(driver);
            for (LogEntry entry: entries) {
                logger.info(String.format("LOG ENTRY: %S", entry.getMessage()));
            }
            Assert.assertFalse(entries.getAll().isEmpty(), "Log entries were empty");
        } else {
            logger.warn("Not looking for logs as we're using an unsupported webdriver!");
            Assert.assertNull(WebDriverLog.getLogEntries(driver));
        }
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "browser"})
    public void compareWindIndicator(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        Assert.assertTrue(mainPage.windIndicatorChanged(driver), "The wind indicator did not change during the interval");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"smoke", "yahoo", "android", "ios", "browser"})
    public void compareSite(AutomationDriver driver) throws InterruptedException {
        initPages(driver);
        Assert.assertTrue(mainPage.hasSiteChanged(), "The site changed during the interval");
    }

    private String getLocalTimeFromEuropeanCity(String city) {
        String localTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/"+city)).format(DateTimeFormatter.ISO_TIME);
        int subStringStart = 0;
        if(localTime.substring(0, 1).equals("0")) {
            subStringStart = 1;
        }
        return localTime.substring(subStringStart, 5);
    }

}
