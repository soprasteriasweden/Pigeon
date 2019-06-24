package se.soprasteria.automatedtesting.webdriver.api.base;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.ConfigurationOption;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.DebugLevel;
import se.soprasteria.automatedtesting.webdriver.api.utility.Credentials;
import se.soprasteria.automatedtesting.webdriver.api.utility.Data;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase.BTCHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase.BaseTestSuite;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.WebDriverLog;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.AppiumHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.AppiumLog;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.MockedData;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.debugimage.ImageEditor;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.Session;
import se.soprasteria.automatedtesting.webdriver.helpers.video.VideoRecording;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * A base test class to extend when creating selenium test cases. It generates a webdriver using a dataprovider and also
 * quits the webdriver after the test has been run. It also initializes configurations, loggers and properties used by
 * tests.
 * <p>
 * Important: Uses propertiesFile and configurationId from the TestNG XML. These specify the path to the properties file
 * and the ID of the webdriver configuration to be used.
 */

@Listeners({BaseTestListener.class})
public abstract class BaseTestCase extends BaseClass {
    /**
     * Main properties file of the project, specified in TestNG XML as a parameter named 'config'
     */
    public BaseTestConfig config;
    /**
     * Credentials helper class that pulls credentials from data file (source specified in config
     */
    public Credentials credentials;
    /**
     * The name of this testsuite, assigned when the class is initialized.
     */
    public String testSuiteName = "n/a";
    /**
     * The configuration ID for the webdriver used in the test, specified in TestNG XML as parameter named 'configurationId'
     */
    public String configurationId;
    /**
     * The propertiesfile path
     **/
    public String propertiesFile;
    /**
     * Mapped testdata to be used in testcases or by dataprovider. Properties should be named test.data.type.name where type
     * is one of the following [string, int, csv] and name is the name of the variable.
     */
    protected Map<String, Object> data;
    /**
     * WebDriver used by BDD testcases to temporarily store the webdriver used in the tests so that it can be torn down
     * after the test
     */
    protected AutomationDriver driver;
    /**
     * The current test name
     */
    public String testname;
    /**
     * The current driver configurations
     */
    public List<DriverConfig> driverConfigs;


    protected BaseTestCase() {
        // Needed to get rid of a warning message related to Selenium logging
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        // Needed to get rid of debug messages related to HttpClient logging
        Configurator.setLevel("org.apache.http", Level.WARN);
        // Needed to get rid of debug messages related to spring framework logging
        Configurator.setLevel("org.springframework", Level.WARN);
    }

    /**
     * Automatically generates the webdriver used by the tests. You import this by specifying the dataprovider for the
     * annotated test to 'getWebDriver'. For more info about usage please refer to reference testcase.
     *
     * @param testMethod Provided by TestNG. Provides information about the testmethod.
     * @return Array with the AutomationDriver object that is being used for the test.
     * @throws java.lang.Exception
     */
    @DataProvider(name="getDriver")
    public Object[][] getDriver(Method testMethod) throws Exception {
        driverConfigs = BTCHelper.getDriverConfigurations(logger, configurationId);
        Object[][] dataToProvide =
                BTCHelper.getWebDriversForDataProvider(driverConfigs,
                        testSuiteName, testMethod.getName());
        if (getTestDataKey() != null) {
            Object[][] updatedList = BTCHelper.addTestData(logger,
                    dataToProvide,
                    data.get(getTestDataKey()));
            if (updatedList != null) {
                dataToProvide = updatedList;
            }
        }

        data = BTCHelper.getData(logger);

        for (Object[] objects : dataToProvide) {
            try {
                driver = ((AutomationDriver) objects[0]);
                initializeDriver(driver);
                initPages(driver);
                goToPageURL(driver);
            } catch(Exception e) {
                logger.debug("Conditions for test initialization could not be met. Please verify that the pre-steps are correctly executed. Closing WebDriver and skipping test.");
                ((AutomationDriver) objects[0]).quit();
                throw e;
            }
        }
        return dataToProvide;
    }

    /**
     * Runs before suite executes, during an execution this function will only be run once, before the first execution
     * of the first method.
     *
     * @param context Contains all the information available on the test about to run.
     * @param xml     Contains the XML configuration used in this testrun.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context, XmlTest xml) {
    }

    /**
     * Automatically runs only once before executing the whole suite. It is used for instance to instantiate runtime
     * environment, such as uploading the app to cloud services that might be used when running the test.
     *
     * @param testContext     Provided by TestNG. Provides information about testrun.
     * @param propertiesFile  Specified in the TestNG XML as a parameter named 'propertiesFile'. The actual file that is
     *                        used need to be in the class path when running the test (preferably resources folder). If
     *                        this value is not present in the XML or empty, the value for DEFAULT_PROPERTY_FILE will be
     *                        used.
     * @param configurationId Specified in the TestNG XML as a parameter named 'configurationId'. The actual file that is
     *                        used need to be in the class path when running the test (preferably resources folder). If
     *                        this value is not present in the XML or empty, the value for DEFAULT_DRIVER_CONFIG will be
     *                        used.
     */
    @Parameters({"propertiesFile", "configurationId"})
    @BeforeTest(alwaysRun = true)
    // Optional is needed in the parameters for this to be run when executing a single test within intellij
    protected void initSuite(final ITestContext testContext,
                             @Optional("") String propertiesFile,
                             @Optional("") String configurationId) {
        config = BaseTestConfig.getInstance(Data.ifEmptyOverride(logger, propertiesFile, getConfigFile()), testContext);
        BaseTestSuite.initializeRuntimeEnvironment(
                BTCHelper.getDriverConfigurations(
                        logger,Data.ifEmptyOverride(logger, configurationId, getDriverConfigId())));
        setConfigurationOptions();
        MockedData.initServerPorts(logger);
    }

    public boolean isTestTargetMobileApp() {
        if(driverConfigs.get(0).capabilities != null) {
            for (DriverConfig.Capability capability: driverConfigs.get(0).capabilities) {
                if (capability.name.contentEquals("app")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Automatically runs before each class is created. Used among other to instantiate properties, webdrivers and
     * credentials used during the test.
     *
     * @param testContext     Provided by TestNG. Provides information about testrun.
     * @param propertiesFile  Specified in the TestNG XML as a parameter named 'propertiesFile'. The actual file that is
     *                        used need to be in the class path when running the test (preferably resources folder). If
     *                        this value is not present in the XML or empty, the value for DEFAULT_PROPERTY_FILE will be
     *                        used.
     * @param configurationId Specified in the TestNG XML as a parameter named 'configurationId'. The actual file that is
     *                        used need to be in the class path when running the test (preferably resources folder). If
     *                        this value is not present in the XML or empty, the value for DEFAULT_DRIVER_CONFIG will be
     *                        used.
     * @param debugLevel      Specified in the TestNG XML as a property with name 'automatedtesting.debug.level'.
     */
    @Parameters({"propertiesFile", "configurationId", "debugLevel"})
    @BeforeClass(alwaysRun = true)
    // Optional is needed in the parameters for this to be run when executing a single test within intellij
    protected void initTestClass(final ITestContext testContext,
                                 @Optional("") String propertiesFile,
                                 @Optional("") String configurationId,
                                 @Optional("") String debugLevel) {
        logger.info("INIT CLASS: " + this.getClass().getSimpleName());
        config = BaseTestConfig.getInstance(Data.ifEmptyOverride(logger, propertiesFile, getConfigFile()));
        this.configurationId = Data.ifEmptyOverride(logger, configurationId, getDriverConfigId());
        this.testSuiteName = testContext.getName();
        if (BaseTestConfig.getInstance().getConfig().users != null) this.credentials = new Credentials();
        DebugLevel.set(Data.ifEmptyOverride(logger, debugLevel, getDebugLevel()));
        Session.setCurrentConfigurationId(this.configurationId);
        startAppium();
    }

    /**
     * Runs before an actual testmethod executes. Sets the methodname for this specific testrun.
     *
     * @param context      Contains all the information available on the test about to run.
     * @param xml          Contains the XML configuration used in this testrun.
     * @param method       Contains a prototype of the method about to run.
     * @param providerData Contains instances of objects instantiated by the dataprovider to the tests.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestContext context, XmlTest xml, Method method, Object[] providerData) {
        if (!driver.isMobile()) driver.manage().window().maximize();
        startVideoRecording();
        testname = method.getName();
        WebDriverLog.printLogFile(logger);
        getDriverWithoutDataProvider(method);
    }

    public void startVideoRecording() {
        VideoRecording.getInstance().startRecording(driver);
    }

    /**
     * Automatically runs after each method and tears down the webdriver.
     *
     * @param testResult Provided by TestNG. Provides information about the performed test.
     */
    @AfterMethod(alwaysRun = true)
    protected void closeWebDriver(ITestResult testResult) {
        AutomationDriver driver = this.driver;
        if (testResult.getParameters().length > 0) {
            driver = (AutomationDriver) testResult.getParameters()[0];
        } else if(driver == null) {
            throw new WebDriverException("Could not find a WebDriver to close!");
        }
        closeWebDriversOpenedByID();
        logger.info("CLOSING " + driver.getWebDriverName().toUpperCase() + ": " + testResult.getName());
        driver.quit();
        if(driver.isMobile() || driver.isWindowsDriver()){
            sleep(2000);
            AppiumLog.printAppiumLogForCurrentTest(testResult, logger);
        }
        WebDriverLog.printLogFile(logger);
        MockedData.releasePort(testname);
        WebDriverLog.clearWebDriverLog(logger);
    }

    /**
     * Automatically runs only once after executing the whole suite. It is used for closing any potential appium
     * instance opened during the test suite.
     */
    @AfterTest(alwaysRun = true)
    protected void closeSuite() {
        stopAppium();
    }

    /**
     * Function that you can override to perform webdriver specific actions before actually starting the test.
     *
     * @param driver
     */
    protected void initializeDriver(AutomationDriver driver) throws Exception {
        // Perform webdriver specific initialisation, eg navigate to webpage or dismiss updates in app
    }

    /**
     * Abstract method that must always be implemented to specify what webdriver that should be used.
     * @return driver config id.
     */
    protected abstract String getDriverConfigId();

    /**
     * Abstract method that must be implemented to specify the path to the config xml file.
     * @return path to property file.
     */
    protected abstract String getConfigFile();


    /**
     * Abstract method that must be implemented for initialization of the page objects.
     * @return path to property file.
     */
    protected abstract void initPages(AutomationDriver driver);

    protected void goToPageURL(AutomationDriver driver) {
        if (!isTestTargetMobileApp()) {
            if (!BaseTestConfig.getConfigurationOption("mainpage.url").equalsIgnoreCase("")) {
                driver.navigate().to(BaseTestConfig.getConfigurationOption("mainpage.url"));
            }
        }
    }


    /**
     * Override this function to specify if a generated appium log should be printed. If not overridden
     * no log file wil be printed. To generate and print a appium log, turn on appium logging by sending appium server
     * argument --log with the path to the root of the project and specify the appium log filename to start with "appium_log"
     * when starting appium.
     * Example: "--log c://projectDirectory/appium_log_MyAppiumLog.log"
     *
     * @return
     */
    protected boolean printAppiumLog() {
        return false;
    }

    /**
     * Override this function to specify data which the dataprovider should provide to the testcase. If not overridden
     * no data will be fed to the test
     *
     * @return A key to a valid property in the property file used by the test
     */
    protected String getTestDataKey() {
        return null;
    }

    /**
     * Get a webdriver using a configuration id. NOTE: These webdrivers will automatically close after each test
     * method.
     *
     * @param configurationId specifying which configuration to use from the driverconfiguration XML
     * @return webdriver
     */
    protected WebDriver getWebDriverById(String configurationId) {
        return BTCHelper.getDriver(logger, configurationId);
    }

    /**
     * Load mocked data based on category and data name.
     *
     * @param driver
     * @param mockedDataCategory The category of mocked data
     * @param mockedDataName     The name of the mocked data
     */
    protected void loadMockedData(AutomationDriver driver, String mockedDataCategory, String mockedDataName) throws Exception {
        performBeforeLoadingMockedData(driver);
        MockedData.loadMockedData(logger, testname, mockedDataCategory, mockedDataName);
        performAfterLoadingMockedData(driver);
    }

    /**
     * Runs before loading the mocked data. This method can be overloaded if actions related to mocked data
     * is needed before loading the mocked data.
     *
     * @param driver
     */
    protected void performBeforeLoadingMockedData(AutomationDriver driver) {
    }

    /**
     * Runs after loading the mocked data. This method can be overloaded if actions related to mocked data
     * is needed after loading the mocked data.
     *
     * @param driver
     */
    protected void performAfterLoadingMockedData(AutomationDriver driver) {
    }

    /**
     * Sets the default debuglevel for the project. This debuglevel is used by the debugger but can be overridden.
     *
     * @return - Default debugtype
     */
    protected String getDebugLevel() {
        return DebugLevel.DEFAULT_LEVEL.name();
    }

    private void setConfigurationOptions() {
        AppiumLog.set(logger, Data.ifEmptyOverrideBoolean(BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_LOG),
                printAppiumLog()));
        ImageEditor.setScreenshotFileFormat(logger, Data.ifEmptyOverride(logger,
                BaseTestConfig.getConfigurationOption(ConfigurationOption.SCREENSHOT_FORMAT), "jpg"));
        try {
            ImageEditor.setScreenshotSizeFactor(logger,
                    Data.ifEmptyOverrideDouble(BaseTestConfig.getConfigurationOption(ConfigurationOption.SCREENSHOT_SIZE),
                            1.0));
        } catch (NumberFormatException e) {
            logger.warn("Screenshot size configuration option value illegal format, needs to be set as a double value");
            throw e;
        }
    }

    private void startAppium() {
        if (Boolean.valueOf(BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_AUTOMATIC)) &&
                BTCHelper.doesConfigurationTypeSupportAppium(logger, this.configurationId)) {
            String port;
            try {
                port = BTCHelper.getConfigurationUrlPort(logger, this.configurationId);
            } catch (RuntimeException e) {
                logger.trace("Failed to extract port from configuration id " + this.configurationId
                        + " when starting Appium, choose a " +
                        "configuration id with a url and port");
                throw e;
            }
            AppiumHelper.startAppium(logger, port);
        }
    }

    private void stopAppium() {
        if (Boolean.valueOf(BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_AUTOMATIC)) &&
                !Boolean.valueOf(BaseTestConfig.getConfigurationOption(ConfigurationOption.APPIUM_KEEP_ALIVE))) {
            AppiumHelper.stopAppium(logger);
        }
    }

    private void closeWebDriversOpenedByID() {
        BTCHelper.closeWebDriversOpenedDuringSession(logger);
    }

    private void getDriverWithoutDataProvider(Method testMethod) {
        boolean noDataProvider = true;
        for (Class<?> parameter : testMethod.getParameterTypes()) {
            if (parameter == AutomationDriver.class) noDataProvider = false;
        }
        if (noDataProvider) {
            logger.warn("WARNING! EXPERIMENTAL FEATURE: Initializing WebDriver without using a DataProvider is an experimental " +
                    "feature and may cause issues. It is recommended to use a DataProvider annotation and WebDriver parameter " +
                    "with your test method.");
            try {
                getDriver(testMethod);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize a WebDriver without a DataProvider: " + e.getMessage());
            }
        }
    }
}
