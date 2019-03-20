package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig.Capability;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.IOSUtils;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.SessionCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * This will initialize the webdriver using a configuration from the JSON in the resources directory.
 */
public class Setup {
    private final Platform currentPlatform = Platform.getCurrent();
    private final String testId;
    private final String testMethodName;
    private final String testSuiteName;
    private final DriverConfig driverConfiguration;
    private final Logger logger = LogManager.getLogger("Setup");
    private AutomationDriver driver;

    public static ArrayList<String> IGNORED_UNSUPPORTED_CAPABILITIES = new ArrayList<>(Arrays.asList(
            "deviceType",
            "version"
    ));

    public static final String OPTION_TYPE_ENCODED_EXTENSION = "encodedExtension";
    public static final String OPTION_TYPE_ARGUMENT = "argument";
    public static final String OPTION_TYPE_EXTENSION = "extension";
    public static final String OPTION_TYPE_EXPERIMENTAL = "experimental";
    public static final String[] VALID_OPTION_TYPES = {
            OPTION_TYPE_ENCODED_EXTENSION,
            OPTION_TYPE_ARGUMENT,
            OPTION_TYPE_EXTENSION,
            OPTION_TYPE_EXPERIMENTAL
    };

    public Setup(DriverConfig driverConfiguration, String testSuiteName, String testMethodName) {
        this.testId = java.util.UUID.randomUUID().toString();
        this.driverConfiguration = driverConfiguration;
        this.testMethodName = testMethodName;
        this.testSuiteName = testSuiteName;
        initializeWebDriver();
    }

    public AutomationDriver getWebDriver() {
        return this.driver;
    }

    private void initializeWebDriver() {
        addIgnoredCapabilities();
        logger.info("Initializing " + driverConfiguration.type);
        switch (driverConfiguration.type) {
            case "FirefoxDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeFirefoxDriver()), driverConfiguration, logger); break;
            case "ChromeDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeChromeDriver()), driverConfiguration, logger);break;
            case "InternetExplorerDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeIEDriver()), driverConfiguration, logger);break;
            case "EdgeDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeEdgeDriver()), driverConfiguration, logger);break;
            case "SafariDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeSafariDriver()), driverConfiguration, logger); break;
            case "OperaDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeOperaDriver()), driverConfiguration, logger);break;
            case "RemoteWebDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeRemoteWebDriver()), driverConfiguration, logger);break;
            case "HtmlUnitDriver":
                this.driver = new AutomationDriver(initializeEventFiringWebDriver(initializeHTMLUnitDriver()), driverConfiguration, logger); break;
            case "AndroidDriver":
                this.driver = new AutomationDriver(initializeAppiumEventFiringWebDriver(initializeAndroidDriver()), driverConfiguration, logger);break;
            case "IOSDriver":
                this.driver = new AutomationDriver(initializeAppiumEventFiringWebDriver(initializeIosDriver()), driverConfiguration, logger);break;
            case "WindowsDriver":
                this.driver = new AutomationDriver(initializeAppiumEventFiringWebDriver(initializeWindowsDriver()), driverConfiguration, logger);break;
            default:
                logger.error("Invalid webdriver specified in configuration");
                throw new RuntimeException("Specified invalid type for webdriver configuration");
        }
        WebDriverLog.initializeWebDriverLog(driver, logger);
        Screenshot.initializeScreenshot(driver, logger);
    }

    private EventFiringWebDriver initializeEventFiringWebDriver(WebDriver webDriver) {
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(webDriver);
        eventFiringWebDriver.register(new EventHandler(testMethodName));
        return eventFiringWebDriver;
    }

    private WebDriver initializeAppiumEventFiringWebDriver(WebDriver webDriver) {
        return EventFiringWebDriverFactory.getEventFiringWebDriver(webDriver, new EventHandler(testMethodName));
    }

    private FirefoxDriver initializeFirefoxDriver() {
        firefoxDriverLogging();
        return new FirefoxDriver(getFirefoxOptions());
    }

    private ChromeDriver initializeChromeDriver() {
        chromeDriverLogging();
        return new ChromeDriver(getChromeOptions());
    }

    private InternetExplorerDriver initializeIEDriver() {
        if (currentPlatform.is(Platform.WINDOWS)) {
            internetExplorerDriverLogging();
            InternetExplorerDriver internetExplorerDriver = new InternetExplorerDriver(getIEOptions());
            return internetExplorerDriver;
        }
        throw new RuntimeException("Failed to initialize InternetExplorerDriver because you appear not to be using Windows.");
    }

    private EdgeDriver initializeEdgeDriver() {
        if (currentPlatform.is(Platform.WINDOWS)) {
            return new EdgeDriver(getEdgeOptions());
        }
        throw new RuntimeException("Failed to initialize EdgeDriver because you appear not to be using Windows."); }

    private SafariDriver initializeSafariDriver() {
        if (currentPlatform.is(Platform.MAC)) {
            return new SafariDriver(getSafariOptions());
        }
        throw new RuntimeException("Failed to initialize SafariDriver " +
                "(most likely either unsupported platform or missing safari extension)");
    }

    private OperaDriver initializeOperaDriver() {
        return new OperaDriver(getOperaOptions());
    }

    private RemoteWebDriver initializeRemoteWebDriver() {
        DesiredCapabilities desiredCapabilities = getRemoteWebDriverCapabilities();
        try {
            return new RemoteWebDriver(new URL(driverConfiguration.url), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize RemoteWebDriver with malformed URL:" +
                    driverConfiguration.url);
        }
    }

    private HtmlUnitDriver initializeHTMLUnitDriver() {
        DesiredCapabilities desiredCapabilities = getHtmlUnitCapabilities();
        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(desiredCapabilities);
        htmlUnitDriver.setJavascriptEnabled(true);
        return htmlUnitDriver;
    }

    private AndroidDriver initializeAndroidDriver() {
        DesiredCapabilities desiredCapabilities = getMobileCapabilities();
        try {
            return new AndroidDriver(new URL(driverConfiguration.url), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize AndroidWebDriver with malformed URL:" +
                    driverConfiguration.url);
        }
    }

    private IOSDriver initializeIosDriver() {
        DesiredCapabilities desiredCapabilities = getMobileCapabilities();
        try {
            return new IOSDriver(new URL(driverConfiguration.url), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(("Failed to initialize IOSDriver with malformed URL:" +
                    driverConfiguration.url));
        }
    }

    private WindowsDriver initializeWindowsDriver() {
        DesiredCapabilities capabilities = getWindowsDriverCapabilities();
        try {
            return new WindowsDriver(new URL(driverConfiguration.url), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(("Failed to initialize WindowsDriver with malformed URL:" +
                    driverConfiguration.url));
        }
    }

    private DesiredCapabilities getWindowsDriverCapabilities(){
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(new DesiredCapabilities());
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        return desiredCapabilities;
    }

    private DesiredCapabilities getRemoteWebDriverCapabilities() {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(new DesiredCapabilities());
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        return desiredCapabilities;
    }

    private DesiredCapabilities getMobileCapabilities() {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(new DesiredCapabilities());
        return desiredCapabilities;
    }

    private DesiredCapabilities getHtmlUnitCapabilities() {
        DesiredCapabilities htmlUnitCapabilities = DesiredCapabilities.htmlUnit();
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(htmlUnitCapabilities);
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        return desiredCapabilities;
    }

    private DesiredCapabilities getIECapabilities() {
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(ieCapabilities);
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        return desiredCapabilities;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.merge(getDesiredCapabilities(DesiredCapabilities.edge()));
        edgeOptions.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        return edgeOptions;
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(getDesiredCapabilities(DesiredCapabilities.chrome()));
        chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());

        if (driverConfiguration.options == null) return chromeOptions;
        for (DriverConfig.Option option : driverConfiguration.options) {
            if (ArrayUtils.contains(VALID_OPTION_TYPES, option.type)) {
                switch (option.type) {
                    case OPTION_TYPE_ARGUMENT:
                        chromeOptions.addArguments(option.value);
                        break;
                    case OPTION_TYPE_ENCODED_EXTENSION:
                        chromeOptions.addEncodedExtensions(option.value);
                        break;
                    case OPTION_TYPE_EXTENSION:
                        chromeOptions.addExtensions(new File(option.value));
                        break;
                    case OPTION_TYPE_EXPERIMENTAL:
                        chromeOptions.setExperimentalOption(option.name, getChromeSwitch(option.name, option.value));
                        break;
                }
            } else {
                boolean value = false;
                if (option.value.contentEquals("true")) value = true;
                logger.debug(String.format("Setting Chrome option %s to %b", option.name, value));

                switch (option.name) {
                    case "runHeadless" :
                        chromeOptions.setHeadless(value);
                        break;
                    case "acceptInsecureCerts" :
                        chromeOptions.setAcceptInsecureCerts(value);
                        break;
                    case "verboseLogging" :
                        if(value && !isDriverLoggingEnabled()) {
                            logger.info("Chrome verbose logging set but not enabled, Debug Level needs to be " +
                                    "set to a DriverLog option");
                            break;
                        }
                        if(value) System.setProperty("webdriver.chrome.verboseLogging", "true");
                        break;
                    default:
                        logger.error(String.format("Chrome Option: %s set but has not been defined!", option.name));
                }
            }
        }
        return chromeOptions;
    }

    private SafariOptions getSafariOptions() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.merge(getDesiredCapabilities(DesiredCapabilities.safari()));
        safariOptions.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());

        if (driverConfiguration.options == null) return safariOptions;
        for (DriverConfig.Option option : driverConfiguration.options) {
            boolean value = false;
            if (option.value.contentEquals("true")) value = true;
            logger.debug(String.format("Setting Safari option %s to %b", option.name, value));

            switch (option.name) {
                case "useTechnologyPreview":
                    safariOptions.setUseTechnologyPreview(value);
                    break;
                default:
                    logger.error(String.format("Safari Option: %s set but has not been defined!", option.name));
            }
        }
        return safariOptions;
    }

    private OperaOptions getOperaOptions() {
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        Boolean operaBinaryPathSet = false;
        try {
            for (DriverConfig.Option option : driverConfiguration.options) {
                logger.debug(String.format("Setting Opera option %s to %s", option.name, option.value));

                switch (option.name) {
                    case "operaBinaryPath":
                        operaOptions.setBinary(option.value);
                        operaBinaryPathSet = true;
                        break;
                    default:
                        logger.error(String.format("Opera Option: %s set but has not been defined!", option.name));
                }
            }
        } catch (NullPointerException e) {
            //TODO Add what action to take if NullPointerException
        }
        if(!operaBinaryPathSet) {
            throw new RuntimeException("The OperaDriver option 'operaBinaryPath' has not be set, a path " +
                    "to the Opera browser binary is needed to automate Opera.");
        }
        return operaOptions;
    }

    private InternetExplorerOptions getIEOptions() {
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        internetExplorerOptions.merge(getIECapabilities());

        if(driverConfiguration.options == null) return internetExplorerOptions;
        for (DriverConfig.Option option : driverConfiguration.options) {
            boolean value = false;
            if (option.value.contentEquals("true")) value = true;
            logger.debug(String.format("Setting Internet Explorer option %s to %b", option.name, value));

            switch (option.name) {
                case "requireWindowFocus":
                    if(value) internetExplorerOptions.requireWindowFocus();
                    break;
                case "enableNativeEvents":
                    if(value) internetExplorerOptions.enableNativeEvents();
                    break;
                case "ignoreZoomSettings":
                    if(value) internetExplorerOptions.ignoreZoomSettings();
                    break;
                default:
                    logger.error(String.format("InternetExplorer Option: %s set but has not been defined!", option.name));
            }
        }
        return internetExplorerOptions;
    }

    private Object getChromeSwitch(String name, String value) {
        // remove newlines, tabs, carriage returns and replace multiple whitespaces with single ones
        value = value.replaceAll("[\\s\\t\\n\\r]", " ").replaceAll("\\s+", " ");
        switch (name) {
            case "args":
            case "extensions":
            case "excludeSwitches":
            case "windowTypes":
                return Arrays.asList(value.split(" "));
            case "localState":
            case "prefs":
            case "mobileEmulation":
            case "perfLoggingPrefs":
                Map<String, Object> prefs = new HashMap<String, Object>();
                for (String keyValuePair : Arrays.asList(value.split(" "))) {
                    prefs.put(keyValuePair.split(",")[0], keyValuePair.split(",")[1]);
                }
                return prefs;
            case "detach":
                if (value == "true") {
                    return true;
                }
                return false;
            default:
                return value;
        }
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("nglayout.initialpaint.delay", 0);
        firefoxOptions.addPreference("browser.tabs.animate", false);
        firefoxOptions.addPreference("image.animation_mode", "none");
        firefoxOptions.addPreference("browser.sessionhistory.max_total_viewer", 1);
        firefoxOptions.addPreference("browser.sessionhistory.max_entries", 3);
        firefoxOptions.addPreference("browser.sessionhistory.max_total_viewers", 1);
        firefoxOptions.addPreference("browser.sessionstore.max_tabs_undo", 0);
        firefoxOptions.addPreference("network.http.pipelining", true);
        firefoxOptions.addPreference("network.http.pipelining.maxrequests", 8);
        firefoxOptions.addPreference("browser.cache.memory.enable", true);
        firefoxOptions.addPreference("browser.cache.disk.enable", true);
        firefoxOptions.addPreference("browser.search.suggest.enabled", false);
        firefoxOptions.addPreference("browser.formfill.enable", false);
        firefoxOptions.addPreference("browser.download.manager.scanWhenDone", false);
        firefoxOptions.addPreference("browser.bookmarks.max_backups", 0);
        firefoxOptions.addPreference("reader.parse-on-load.enabled",false);

        firefoxOptions.merge(getDesiredCapabilities(DesiredCapabilities.firefox()));
        firefoxOptions.setCapability(CapabilityType.LOGGING_PREFS, getLoggingPrefs());
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);

        if(driverConfiguration.options == null) return firefoxOptions;
        for (DriverConfig.Option option : driverConfiguration.options) {
            boolean value = false;
            if (option.value.contentEquals("true")) value = true;
            logger.debug(String.format("Setting Firefox option %s to %b", option.name, value));

            switch (option.name) {
                case "runHeadless":
                    firefoxOptions.setHeadless(value);
                    break;
                case "acceptInsecureCerts" :
                    firefoxOptions.setAcceptInsecureCerts(value);
                    break;
                default:
                    logger.error(String.format("Firefox Option: %s set but has not been defined!", option.name));
            }
        }
        return firefoxOptions;
    }

    private DesiredCapabilities getDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        if (driverConfiguration.capabilities != null) {
            for (Capability capability: driverConfiguration.capabilities) {
                setCapability(desiredCapabilities, capability);
            }
        }
        return desiredCapabilities;
    }

    private void setCapability(DesiredCapabilities desiredCapabilities, String name, String value) {
        if(!IGNORED_UNSUPPORTED_CAPABILITIES.contains(name)){
            logger.info("Setting capability: " + name + "   value: " + value);
            if(value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
                desiredCapabilities.setCapability(name, Boolean.valueOf(value));
            } else {
                desiredCapabilities.setCapability(name, value);
            }
        }
    }

    private void setCapability(DesiredCapabilities desiredCapabilities, Capability capability) {
        String value = capability.getValue();
        if (capability.alternateValue != null) {
            value = capability.alternateValue;
        }
        setCapability(desiredCapabilities, capability.name, value);
        setSessionCapability(capability);
        if (capability.name.contentEquals("app")) {
            if (value.endsWith(".apk")) {
                androidApkFound(desiredCapabilities, capability);
            } else if (value.endsWith(".ipa")) {
                iosIpaFound(desiredCapabilities, capability);
            } else if (value.endsWith("!App")) {
                windowsAppFound(desiredCapabilities, capability);
            }
        }
    }

    private LoggingPreferences getLoggingPrefs() {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        return logPrefs;
    }

    private void androidApkFound(DesiredCapabilities desiredCapabilities, Capability capability) {
        String appPath = capability.getValue();
        try (ApkFile apkFile = new ApkFile(new File(appPath))) {
            ApkMeta metaData = apkFile.getApkMeta();
            logger.info(String.format("\n----- ANDROID PACKAGE INFORMATION ------\n"
                + " Package Label: %s\n"
                + " Version Name: %s\n"
                + " Version Code: %s\n"
                + "----------------------------------------"
                ,metaData.getLabel(), metaData.getVersionName(), metaData.getVersionCode()));
        }
        catch (IOException ioe) {
            logger.error(String.format("Detected android package but couldn't read .apk file\n%s", ioe.getMessage()));
        }
    }

    private void iosIpaFound(DesiredCapabilities desiredCapabilities, Capability capability) {
        String appPath = capability.getValue();
        Map<String, String> ipaInfo = IOSUtils.getIPAInfo(appPath);
        logger.info(String.format("\n----- IOS PACKAGE INFORMATION ------\n"
                        + " Package Label: %s\n"
                        + " Version Name: %s.%s\n"
                        + "---------------------------------------"
                ,ipaInfo.get("CFBundleName"), ipaInfo.get("CFBundleShortVersionString"), ipaInfo.get("CFBundleVersion")));
    }

    private void windowsAppFound(DesiredCapabilities desiredCapabilities, Capability capability) {
        //TODO: Get and print app info from the windows app that's being tested
    }

    private void checkLogFilePath() {
        File surefireReportsDir = new File("target/surefire-reports");
        if(!surefireReportsDir.exists()) {
            surefireReportsDir.mkdir();
        }
    }

    private void chromeDriverLogging() {
        if(isDriverLoggingEnabled()) {
            checkLogFilePath();
            System.setProperty("webdriver.chrome.logfile", "target/surefire-reports/chromedriver.log");
        }
    }

    private void internetExplorerDriverLogging() {
        if(isDriverLoggingEnabled()) {
            checkLogFilePath();
            System.setProperty("webdriver.ie.driver.loglevel", "DEBUG");
            System.setProperty("webdriver.ie.driver.logfile", "target/surefire-reports/iedriver.log");
        }
    }

    private void firefoxDriverLogging() {
        if(isDriverLoggingEnabled()) {
            checkLogFilePath();
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "target/surefire-reports/firefoxdriver.log");
        } else {
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        }
    }

    private boolean isDriverLoggingEnabled() {
        return WebDriverLog.driverLoggingEnabled();
    }

    private void addIgnoredCapabilities() {
        ArrayList<String> ignoredUnsupportedCapabilities = new ArrayList<>(Arrays.asList(
                BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_UNSUPPORTED_CAPABILITIES).split(",[ ]*")
        ));
        if(!ignoredUnsupportedCapabilities.isEmpty() && ignoredUnsupportedCapabilities.get(0).length() > 0) {
            for(String capability : ignoredUnsupportedCapabilities) {
                IGNORED_UNSUPPORTED_CAPABILITIES.add(capability);
            }
        }
    }

    private void setSessionCapability(Capability capability) {
        SessionCapabilities.setCapability(capability);
    }
}