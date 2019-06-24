package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.battery.BatteryInfo;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.ws.StringWebSocketClient;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Direction;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.WebDriverType;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestlistener.BTLHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.AppiumHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.SessionCapabilities;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static se.soprasteria.automatedtesting.webdriver.api.datastructures.Direction.*;

public class AutomationDriver implements WebDriver, TakesScreenshot {

    private final WebDriver webDriver;
    private final WebDriverType webDriverType;
    private final Logger logger;
    private final DriverActions driverActions;

    public AutomationDriver(WebDriver webDriver, DriverConfig driverConfiguration, Logger logger) {
        this.webDriver = webDriver;
        webDriverType = Info.getWebDriverType(webDriver);
        this.logger = logger;

        //Call order here is important
        setTimeoutsForWebDriver(driverConfiguration);
        driverActions = new DriverActions(this);
    }

    private void setTimeoutsForWebDriver(DriverConfig driverConfiguration) {
        if (driverConfiguration.implicit < 0) {
            driverConfiguration.implicit = 0;
        }

        if (driverConfiguration.pageLoad <= 0) {
            driverConfiguration.pageLoad = 45000;
        }
        webDriver.manage().timeouts().implicitlyWait(driverConfiguration.implicit,
                TimeUnit.MILLISECONDS);
        if (isWeb()) {
            webDriver.manage().timeouts().pageLoadTimeout(driverConfiguration.pageLoad,
                    TimeUnit.MILLISECONDS);
        }
        System.setProperty("webdriver.implicit.timeout", Integer.toString(driverConfiguration.implicit));
        logger.info("Setting implicit wait to " + driverConfiguration.implicit + " milliseconds");
    }


    /*****************************************
     *****************************************
     *********** WebDriver methods ***********
     *****************************************
     *****************************************/

    @Override
    public void get(String s) {
        webDriver.get(s);
    }

    @Override
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    @Override
    public void close() {
        webDriver.close();
    }

    @Override
    public void quit() {
        webDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return webDriver.navigate();
    }

    @Override
    public Options manage() {
        return webDriver.manage();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return ((TakesScreenshot) webDriver).getScreenshotAs(outputType);
    }

    public void navigateTo(String URL) {
        navigate().to(URL);
    }


    /*****************************************
     *****************************************
     *********  AppiumDriver methods *********
     *****************************************
     *****************************************/

    /**
     * Retrieves battery info from Android or IOS device under test.
     *
     * @return battery info object
     */
    public BatteryInfo getBatteryInfo() {
        try {
            return getAndroidDriver().getBatteryInfo();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getIOSDriver().getBatteryInfo();
        } catch (UnsupportedOperationException ignored) {
        }
        throw new UnsupportedOperationException(getWebDriverName() + " does not support battery info");
    }

    /**
     * Return capabilities from Android, IOS or WindowsDriver that were provided on instantiation.
     *
     * @return capabilities
     */
    public Capabilities getCapabilities() {
        try {
            return getAndroidDriver().getCapabilities();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getIOSDriver().getCapabilities();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getWindowsDriver().getCapabilities();
        } catch (UnsupportedOperationException ignored) {
        }
        throw new UnsupportedOperationException(getWebDriverName() + " can not return capabilities");
    }

    /**
     * @return log client for either Android logcat or IOS syslog
     */
    public StringWebSocketClient getLogClient() {
        try {
            return getAndroidDriver().getLogcatClient();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getIOSDriver().getSyslogClient();
        } catch (UnsupportedOperationException ignored) {
        }
        throw new UnsupportedOperationException(getWebDriverName() + " does not support system log clients");
    }

    /**
     * @return Get the current context in which Appium is running
     */
    public String getContext() {
        try {
            return getAndroidDriver().getContext();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getIOSDriver().getContext();
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return getWindowsDriver().getContext();
        } catch (UnsupportedOperationException ignored) {
        }
        throw new UnsupportedOperationException(getWebDriverName() + " cannot return current context");
    }


    /*****************************************
     *****************************************
     ********* AndroidDriver methods *********
     *****************************************
     *****************************************/

    /**
     * Set the `ignoreUnimportantViews` setting on Android.
     *
     * @param compress ignores unimportant views if true, doesn't ignore otherwise.
     */
    public void ignoreUnimportantViews(boolean compress) {
        getAndroidDriver().ignoreUnimportantViews(compress);
    }

    /**
     * Open the notification shade, on Android devices.
     */
    public void openNotifications() {
        getAndroidDriver().openNotifications();
    }

    /**
     * Toggle location services on Android.
     */
    public void toggleLocationServices() {
        getAndroidDriver().toggleLocationServices();
    }


    /*****************************************
     *****************************************
     *********** IOSDriver methods ***********
     *****************************************
     *****************************************/

    /**
     * Set the `nativeWebTap` setting on IOS
     * @param bool - boolean to set to enabled mode.
     */
    public void nativeWebTap(boolean bool) {
        getIOSDriver().nativeWebTap(bool);
    }

    /**
     * Runs the current IOS app as a background app for the number of seconds or minimizes the app.
     *
     * @param duration The time to run App in background.
     */
    public void runAppInBackground(Duration duration) {
        getIOSDriver().runAppInBackground(duration);
    }


    /*****************************************
     *****************************************
     ************ Helper methods *************
     *****************************************
     *****************************************/

    /**
     * @return AppiumDriver if the AutomationDriver is an AppiumDriver, otherwise throw UnsupportedOperationException
     */
    public AppiumDriver getAppiumDriver() {
        if (Info.isAndroid(webDriverType) || Info.isIos(webDriverType) || Info.isWindows(webDriverType)) {
            return (AppiumDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not an ApiumDriver");
        }
    }

    /**
     * @return MobileDriver if the AutomationDriver is a MobileDriver, otherwise throw UnsupportedOperationException
     */
    public MobileDriver getMobileDriver() {
        if (Info.isAndroid(webDriverType) || Info.isIos(webDriverType)) {
            return (MobileDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not a MobileDriver");
        }
    }

    /**
     * @return AndroidDriver if the AutomationDriver is an AndroidDriver, otherwise throw UnsupportedOperationException
     */
    public AndroidDriver getAndroidDriver() {
        if (Info.isAndroid(webDriverType)) {
            return (AndroidDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not an AndroidDriver");
        }
    }

    /**
     * @return IOSDriver if the AutomationDriver is an IOSDriver, otherwise throw UnsupportedOperationException
     */
    public IOSDriver getIOSDriver() {
        if (Info.isIos(webDriverType)) {
            return (IOSDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not an IOSDriver");
        }
    }

    /**
     * @return WindowsDriver if the AutomationDriver is a WindowsDriver, otherwise throw UnsupportedOperationException
     */
    public WindowsDriver getWindowsDriver() {
        if (Info.isWindows(webDriverType)) {
            return (WindowsDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not a WindowsDriver");
        }
    }

    /**
     * @return RemoteWebDriver if the AutomationDriver is a RemoteWebDriver, otherwise throw UnsupportedOperationException
     */
    public RemoteWebDriver getRemoteWebDriver() {
        if (Info.isRemoteWebDriver(webDriver)) {
            return (RemoteWebDriver) webDriver;
        } else {
            throw new UnsupportedOperationException(Info.getWebDriverName(webDriver) + " is not a RemoteWebDriver");
        }
    }

    /**
     * @return The webdriver instance that the AutomationDriver object wraps.
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Set the implicit timeout for the webdriver. The implicit timeout determines how long the browser will wait before
     * throwing an ElementNotFound exception when performing findElement type operations.
     *
     * @param timeout in millis (changed from seconds to milliseconds in 1.3.6+ versions)
     */
    public void setImplicitTimeout(int timeout) {
        driverActions.setImplicitTimeout(timeout);
    }

    /**
     * @return the currently set implicit timeout for the webdriver.
     */
    public int getImplicitTimeout() {
        return driverActions.getImplicitTimeout();
    }

    /**
     * Enable implicit timeout
     */
    public void enableImplicitTimeout() {
        driverActions.enableImplicitTimeout();
    }

    /**
     * Disable implicit timeout
     */
    public void disableImplicitTimeout() {
        driverActions.disableImplicitTimeout();
    }

    /**
     * reset the implicit timeout to the value it was set to when this class was instantiated.
     */
    public void resetTimeout() {
        driverActions.restoreOriginalImplicitTimeout();
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window.
     *
     * @param script The arguments to the script. May be empty
     * @param args -
     * @return potential return value of the script
     */
    public Object executeJavaScript(String script, Object... args) {
        return driverActions.executeJavaScript(webDriver, script, args);
    }

    /**
     * @return if the passed WebDriver is an AndroidDriver.
     */
    public boolean isAndroid() {
        return Info.isPlatform(webDriver, WebDriverType.AndroidDriver);
    }

    /**
     * @return if the passed WebDriver is an IOSDriver.
     */
    public boolean isIos() {
        return Info.isPlatform(webDriver, WebDriverType.IOSDriver);
    }

    /**
     * @return if the passed WebDriver is NOT an AndroidDriver, IosDriver or WindowsDriver.
     */
    public boolean isWeb() {
        return Info.isWeb(webDriver);
    }


    /**
     * @return if the passed WebDriver is an AndroidDriver or IosDriver.
     */
    public boolean isMobile() {
        return Info.isMobile(webDriver);
    }

    /**
     * @return if the passed WebDriver is a ChromeDriver
     */
    public boolean isChrome() {
        return Info.isPlatform(webDriver, WebDriverType.ChromeDriver);
    }

    /**
     * @return if the passed WebDriver is a FirefoxDriver
     */
    public boolean isFirefox() {
        return Info.isPlatform(webDriver, WebDriverType.FirefoxDriver);
    }

    /**
     * @return if the passed WebDriver is an InternetExplorerDriver
     */
    public boolean isInternetExplorer() {
        return Info.isInternetExplorer(webDriverType);
    }

    /**
     * @return if the passed WebDriver is an EdgeDriver
     */
    public boolean isEdge() {
        return Info.isEdge(webDriverType);
    }

    /**
     * @return if the passed WebDriver is a RemoteWebDriver
     */
    public boolean isRemoteWebDriver() {
        return Info.isRemoteWebDriver(webDriverType);
    }

    /**
     * @return if the passed WebDriver is an SafariDriver
     */
    public boolean isSafari() {
        return Info.isSafari(webDriverType);
    }

    /**
     * @return if the passed WebDriver is an WindowsDriver
     */
    public boolean isWindowsDriver() {
        return Info.isWindows(webDriverType);
    }

    /**
     * @return if the passed WebDriver is an HtmlUnitDriver
     */
    public boolean isHtmlUnitDriver() {
        return Info.isHtmlUnit(webDriverType);
    }

    /**
     * @return if the passed webdriver is an opera driver
     */
    public boolean isOpera() {
        return Info.isOpera(webDriverType);
    }

    /**
     * @param webDriverType to be passed
     * @return true if WebDriver is of passed WebDriverType
     */
    public boolean isPlatform(WebDriverType webDriverType) {
        return Info.isPlatform(webDriver, webDriverType);
    }

    /**
     * @return WebDriverType of WebDriver
     */
    public WebDriverType getWebDriverType() {
        return webDriverType;
    }

    /**
     * @return WebDriver name as a String
     */
    public String getWebDriverName() {
        return webDriverType.toString();
    }

    /**
     * Writes the DOM tree of the webdriver to the specified file.
     *
     * @param logger    Logger for debug logging
     * @param webDriver WebDriver from which to capture DOM tree
     * @param filename  Filename of the output file (path is defaulted to target/surefire-reports)
     */
    public void writeSourceToFile(Logger logger, WebDriver webDriver, String filename) {
        BTLHelper.savePageSource(logger, this, filename);
    }

    /**
     * Wait a set amount of time while refreshing current session, mainly to prevent active logins to time out
     *
     * @param millisWait            Time in milliseconds to wait
     * @param millisRefreshInterval How often in milliseconds the refresh will occur
     */
    public void waitAndKeepSessionActive(int millisWait, int millisRefreshInterval) {
        driverActions.waitAndKeepSessionActive(millisWait, millisRefreshInterval);
    }

    /**
     * Wait a set amount of time while clicking a WebElement, mainly to prevent active logins to time out
     *
     * @param millisWait          Time in milliseconds to wait
     * @param millisClickInterval How often in milliseconds the click will occur
     * @param webElement          WebElement to click
     */
    public void waitAndKeepSessionActive(int millisWait, int millisClickInterval, WebElement webElement) {
        driverActions.waitAndKeepSessionActive(millisWait, millisClickInterval, webElement);
    }

    /**
     * Wait a set amount of time while clicking two elements subsequently, mainly to prevent active logins to time out
     *
     * @param millisWait          Time in milliseconds to wait
     * @param millisClickInterval How often in milliseconds the click will occur
     * @param firstWebElement     The first WebElement to click
     * @param secondWebElement    The second WebElement to click
     */
    public void waitAndKeepSessionActive(int millisWait, int millisClickInterval, WebElement firstWebElement, WebElement secondWebElement) {
        driverActions.waitAndKeepSessionActive(millisWait, millisClickInterval, firstWebElement, secondWebElement);
    }

    /**
     * Do an image comparison of the screen before and after the given interval. Returns false if identical.
     *
     * @param timeoutMillis time interval to compare the screen
     * @return true if screen changed after given interval, false if not.
     */
    public boolean didScreenChangeDuringInterval(int timeoutMillis) {
        return Screenshot.didScreenChangeDuringInterval(timeoutMillis);
    }

    /**
     * Do an image comparison of the screen before and after the given interval.
     *
     * @param similarityPercentageThreshold threshold in percentage
     * @param timeoutMillis                 time interval to compare the screen
     * @return false if the given threshold has been exceeded, true if not.
     *
     */
    public boolean didScreenChangeDuringInterval(float similarityPercentageThreshold, int timeoutMillis) {
        return Screenshot.didScreenChangeDuringInterval(similarityPercentageThreshold, timeoutMillis);
    }

    /**
     * @return if driver logging have been enabled.
     */
    public boolean isDriverLoggingEnabled() {
        return WebDriverLog.driverLoggingEnabled();
    }

    /**
     * Get the available contexts for a Android, iOS or Windows app.
     *
     * @param timeOutMillis time out before search ends
     * @return Available contexts for a Android, iOS or Windows app
     */
    public Set<String> getAvailableContexts(int timeOutMillis) {
        return DriverActions.getAvailableContexts(webDriver, timeOutMillis);
    }

    /**
     * Get value of the provided capability name.
     *
     * @param name name of capability
     * @return value of the provided capability name.
     */
    public String getCapability(String name) {
        return SessionCapabilities.getCapability(name);
    }

    /**
     * Swipes the screen of the device according to specifications.
     *
     * @param direction                 - The direction of the swipe
     * @param swipeLocationInPercentage - The location in percentage, relative to screen size, where to swipe the screen
     * @param swipeLengthInPercentage   - The length of the swipe in percentage, relative to screen size
     * @param speed                     - The speed of the swipe
     */
    public void swipe(Direction direction, int swipeLocationInPercentage, int swipeLengthInPercentage, Speed speed) {
        AppiumHelper.swipe(this, this.logger, direction, swipeLocationInPercentage, swipeLengthInPercentage, speed);
    }

    /**
     * Swipes the screen of the device downwards according to specifications.
     *
     * @param swipeLocationInPercentage - The location in percentage, relative to screen size, where to swipe the screen
     * @param swipeLengthInPercentage   - The length of the swipe in percentage, relative to screen size
     * @param speed                     - The speed of the swipe
     */
    public void swipeDown(int swipeLocationInPercentage, int swipeLengthInPercentage, Speed speed) {
        swipe(DOWN, swipeLocationInPercentage, swipeLengthInPercentage, speed);
    }

    /**
     * Swipes the screen of the device upwards according to specifications.
     *
     * @param swipeLocationInPercentage - The location in percentage, relative to screen size, where to swipe the screen
     * @param swipeLengthInPercentage   - The length of the swipe in percentage, relative to screen size
     * @param speed                     - The speed of the swipe
     */
    public void swipeUp(int swipeLocationInPercentage, int swipeLengthInPercentage, Speed speed) {
        swipe(UP, swipeLocationInPercentage, swipeLengthInPercentage, speed);
    }

    /**
     * Swipes the screen of the device leftwards according to specifications.
     *
     * @param swipeLocationInPercentage - The location in percentage, relative to screen size, where to swipe the screen
     * @param swipeLengthInPercentage   - The length of the swipe in percentage, relative to screen size
     * @param speed                     - The speed of the swipe
     */
    public void swipeLeft(int swipeLocationInPercentage, int swipeLengthInPercentage, Speed speed) {
        swipe(LEFT, swipeLocationInPercentage, swipeLengthInPercentage, speed);
    }

    /**
     * Swipes the screen of the device rightwards according to specifications.
     *
     * @param swipeLocationInPercentage - The location in percentage, relative to screen size, where to swipe the screen
     * @param swipeLengthInPercentage   - The length of the swipe in percentage, relative to screen size
     * @param speed                     - The speed of the swipe
     */
    public void swipeRight(int swipeLocationInPercentage, int swipeLengthInPercentage, Speed speed) {
        swipe(RIGHT, swipeLocationInPercentage, swipeLengthInPercentage, speed);
    }

    /**
     * This method check all URLs on the website and make a validation first if it is empty or begins
     * with http / https and then check if all pages / images are loaded and have a response code 200.ok
     *
     * @param driver    to use and go through log messages with.
     * @param tagName   The method can test all <a> tags with <href> attribute,
     *                  It can also test all <img> tags with <src> attributes.
     * @param attribute
     * @return Return false if all links are correct.
     */
    public boolean isAllHrefResponding(WebDriver driver, String tagName, String attribute) {
        return driverActions.isAllHrefResponding(driver, tagName, attribute);
    }

    /**
     * This method can include a url website and extract text between tags.
     *
     * @param webDriver to use and get the current url.
     * @param tagName   The method can test all <a> and <li> tags.
     * @return return false if can find a list and its not empty.
     * @throws IOException
     */
    public boolean isListTagNameValue(WebDriver webDriver, String tagName) throws IOException {
        return driverActions.isListTagNameValue(webDriver, tagName);
    }

    /**
     * This method is a help method for (isListTagNameValue).
     *
     * @param url     url from the website that is inserted.
     * @param tagName The specific tag name will extract text from.
     * @return A list of all text String.
     */
    public List<String> getTagValues(String url, String tagName) {
        return driverActions.getTagValues(url, tagName);
    }

    /**
     * This method is a help method for (isListTagNameValue),
     * takes url from website and change it to string.
     *
     * @param requestURL url from the website that is inserted.
     * @return
     * @throws IOException
     */
    public String readStringFromURL(String requestURL) throws IOException {
        return driverActions.readStringFromURL(requestURL);
    }

}
