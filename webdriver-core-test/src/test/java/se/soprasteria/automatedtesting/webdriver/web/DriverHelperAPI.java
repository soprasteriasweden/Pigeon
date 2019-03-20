package se.soprasteria.automatedtesting.webdriver.web;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import static se.soprasteria.automatedtesting.webdriver.api.datastructures.WebDriverType.*;
import static se.soprasteria.automatedtesting.webdriver.web.datastructure.Page.MAIN_PAGE;

public class DriverHelperAPI extends WebBaseTestCase {
    
    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    public void getCapabilityTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        String capability = "deviceName";
        String capabilityValue = driverMethods.getCapability(capability);
        Assert.assertFalse(driverMethods.isStringNullOrEmpty(capabilityValue), "Capability " + capability + " was not present");
        logger.debug("The capability \"" + capability + "\" was present with the value \"" + capabilityValue + "\"");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void getAvailableContextsTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.correctContextsAvailable(), "Incorrect contexts are available");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void isDriverLoggingEnabledTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertEquals(driverMethods.isDriverLoggingEnabled(), driverMethods.doesDebugLevelParameterContainDriverLog(),
                "The set debug level does not coincide with the method return of isDriverLoggingEnabled");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void didScreenChangeDuringIntervalTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        obviateMouse();
        Assert.assertTrue(mainPage.isPageLoaded(),"Page was not loaded correctly");
        if(driver.isAndroid()) sleep(2000); //Sleep to give android scroll bar time to fade out
        Assert.assertFalse(driverMethods.didScreenChange(), "Screen changed when it shouldn't have");
        clickElementMethods.clickTimerShowButton();
        Assert.assertTrue(driverMethods.didScreenChange(), "Screen did not change when it should have");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void didScreenChangeDuringIntervalWithPercentageTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        obviateMouse();
        clickElementMethods.clickTimerShowButton();
        clickElementMethods.clickTimerHideButton();
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertFalse(driverMethods.didScreenChangeMoreThanAllowedThreshold(), "Screen changed more than it should have");
        elementScreenshotPage.navigateTo();
        elementScreenshotPage.clickImageRect();
        Assert.assertTrue(driverMethods.didScreenChangeMoreThanAllowedThreshold(), "Screen changed less then it should have");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void waitAndKeepSessionActiveTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        sleep(15000);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        driverMethods.waitAndKeepSessionActive();
        Assert.assertTrue(isElementMethods.loggedIn(), "Was not logged in when you should have been.");

    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void waitAndKeepSessionActiveOneElementTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        sleep(15000);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        driverMethods.waitAndKeepSessionActiveOneElement();
        Assert.assertTrue(isElementMethods.loggedIn(), "Was not logged in when you should have been.");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void waitAndKeepSessionActiveTwoElementsTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        sleep(15000);
        Assert.assertFalse(isElementMethods.loggedIn(), "Was logged in when you shouldn't have");
        clickElementMethods.logIn();
        driverMethods.waitAndKeepSessionActiveTwoElements();
        Assert.assertTrue(isElementMethods.loggedIn(), "Was not logged in when you should have been.");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void savePageSourceTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        driverMethods.savePageSource();
        Assert.assertTrue(driverMethods.validatePageSourceExistence(), "Page source file did not exist");
        Assert.assertTrue(driverMethods.validatePageSourceContent(), "Page source content did not validate correctly");
        driverMethods.deletePageSourceFile();
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"chrome"})
    public void platformChromeTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(ChromeDriver), "The platform was not ChromeDriver");
        Assert.assertTrue(driverMethods.isChrome(), "The WebDriver was not a ChromeDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "ChromeDriver", "The WebDriver name did not match 'ChromeDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), ChromeDriver, "The WebDriver type was not of ChromeDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"edge"})
    public void platformEdgeTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(EdgeDriver), "The platform was not EdgeDriver");
        Assert.assertTrue(driverMethods.isEdge(), "The WebDriver was not a EdgeDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "EdgeDriver", "The WebDriver name did not match 'EdgeDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), EdgeDriver, "The WebDriver type was not of EdgeDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"firefox"})
    public void platformFirefoxTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(FirefoxDriver), "The platform was not FirefoxDriver");
        Assert.assertTrue(driverMethods.isFirefox(), "The WebDriver was not a FirefoxDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "FirefoxDriver", "The WebDriver name did not match 'FirefoxDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), FirefoxDriver, "The WebDriver type was not of FirefoxDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"internetexplorer"})
    public void platformInternetExplorerTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(InternetExplorerDriver), "The platform was not InternetExplorerDriver");
        Assert.assertTrue(driverMethods.isExplorer(), "The WebDriver was not a InternetExplorerDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "InternetExplorerDriver", "The WebDriver name did not match 'InternetExplorerDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), InternetExplorerDriver, "The WebDriver type was not of InternetExplorerDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"safari"})
    public void platformSafariTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(SafariDriver), "The platform was not SafariDriver");
        Assert.assertTrue(driverMethods.isSafari(), "The WebDriver was not a SafariDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "SafariDriver", "The WebDriver name did not match 'SafariDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), SafariDriver, "The WebDriver type was not of SafariDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"opera"})
    public void platformOperaTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(OperaDriver), "The platform was not OperaDriver");
        Assert.assertTrue(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertEquals(driverMethods.getWebDriverName(), "OperaDriver", "The WebDriver name did not match 'OperaDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), OperaDriver, "The WebDriver type was not of OperaDriver type");
        Assert.assertTrue(driverMethods.isWeb(), "The WebDriver was not of a web platform");
        Assert.assertFalse(driverMethods.isMobile(), "The WebDriver was a mobile type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was not a SafariDriver");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"ios"})
    public void platformIosTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(IOSDriver), "The platform was not IOSDriver");
        Assert.assertTrue(driverMethods.isIos(), "The WebDriver was not a IOSDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "IOSDriver", "The WebDriver name did not match 'IOSDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), IOSDriver, "The WebDriver type was not of IOSDriver type");
        Assert.assertTrue(driverMethods.isMobile(), "The WebDriver was not of a mobile platform");
        Assert.assertFalse(driverMethods.isWeb(), "The WebDriver was a web type when it shouldn't be");
        Assert.assertFalse(driverMethods.isAndroid(), "The WebDriver was an AndroidDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was a ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    public void platformAndroidTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(driverMethods.isPlatform(AndroidDriver), "The platform was not AndroidDriver");
        Assert.assertTrue(driverMethods.isAndroid(), "The WebDriver was not a AndroidDriver");
        Assert.assertEquals(driverMethods.getWebDriverName(), "AndroidDriver", "The WebDriver name did not match 'AndroidDriver'");
        Assert.assertEquals(driverMethods.getWebDriverType(), AndroidDriver, "The WebDriver type was not of AndroidDriver type");
        Assert.assertTrue(driverMethods.isMobile(), "The WebDriver was not of a mobile platform");
        Assert.assertFalse(driverMethods.isWeb(), "The WebDriver was a web type when it shouldn't be");
        Assert.assertFalse(driverMethods.isChrome(), "The WebDriver was an ChromeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isIos(), "The WebDriver was a IOSDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isFirefox(), "The WebDriver was a FirefoxDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isEdge(), "The WebDriver was a EdgeDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isSafari(), "The WebDriver was a SafariDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isExplorer(), "The WebDriver was a InternetExplorerDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isOpera(), "The WebDriver was a OperaDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isWindowsDriver(), "The WebDriver was a WindowsDriver when it shouldn't be");
        Assert.assertFalse(driverMethods.isRemoteWebDriver(), "The WebDriver was a RemoteWebDriver when it shouldn't be");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void executeJavaScriptTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        driverMethods.clickShowElementWithJavascript();
        Assert.assertTrue(isElementMethods.isClickableElementDisplayed(),"Element not displayed when it should have been");
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"browser", "mobile"})
    public void implicitTimeoutTest(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertNotEquals(driverMethods.getImplicitTimeout(), null);
        int originalImplicitTimeout = driverMethods.getImplicitTimeout();
        int newImplicitTimeout = 1000;
        driverMethods.setImplicitTimeout(newImplicitTimeout);
        Assert.assertEquals(driverMethods.getImplicitTimeout(), newImplicitTimeout);
        driverMethods.resetImplicitTimeout();
        Assert.assertEquals(driverMethods.getImplicitTimeout(), originalImplicitTimeout);
    }


/*
    public void waitForAngularRequestToFinish()
    Wait for Angular to finish async activity using Angular default root selector "[ng-app]".

    waitForAngularRequestToFinish
    public void waitForAngularRequestToFinish(java.lang.String rootSelector)
    Wait for Angular to finish async activity, specifying root selector to find Angular app root element. Specifying
    the root selector is needed if the root is not equal to the default root element "[ng-app]", the selector String
    needed is a CSS selector.
*/

}
