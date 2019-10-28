package se.soprasteria.automatedtesting.webdriver.api.base.api;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.utility.Credentials;
import se.soprasteria.automatedtesting.webdriver.api.utility.Data;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.MockedData;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.Session;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * A base test class to extend when creating selenium test cases. It generates a webdriver using a dataprovider and also
 * quits the webdriver after the test has been run. It also initializes configurations, loggers and properties used by
 * tests.
 * <p>
 * Important: Uses propertiesFile and configurationId from the TestNG XML. These specify the path to the properties file
 * and the ID of the webdriver configuration to be used.
 */

@Listeners({ApiBaseTestListener.class})
public abstract class ApiBaseTestCase extends BaseClass {
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
    public String configId;
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


    protected ApiBaseTestCase() {
      /*  // Needed to get rid of a warning message related to Selenium logging
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        // Needed to get rid of debug messages related to HttpClient logging
        Configurator.setLevel("org.apache.http", Level.WARN);
        // Needed to get rid of debug messages related to spring framework logging
        Configurator.setLevel("org.springframework", Level.WARN);*/
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
        this.config = BaseTestConfig.getInstance(Data.ifEmptyOverride(logger, propertiesFile, getConfigFile()));
        this.configId = Data.ifEmptyOverride(logger, configurationId, getDriverConfigId());

        this.testSuiteName = testContext.getName();
        if (BaseTestConfig.getInstance().getConfig().users != null) this.credentials = new Credentials();
        Session.setCurrentConfigurationId(this.configId);
        // DebugLevel.set(Data.ifEmptyOverride(logger, debugLevel, getDebugLevel()));
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
        initPages(driver);
        initializeTest(driver);
    }

    protected void initializeTest(AutomationDriver driver) {
    }

    ;

    /**
     * Automatically runs after each method and tears down the webdriver.
     *
     * @param testResult Provided by TestNG. Provides information about the performed test.
     */
    @AfterMethod(alwaysRun = true)
    protected void closeWebDriver(ITestResult testResult) {
    }

    /**
     * Automatically runs only once after executing the whole suite. It is used for closing any potential appium
     * instance opened during the test suite.
     */
    @AfterTest(alwaysRun = true)
    protected void closeSuite() {
    }

    /**
     * Function that you can override to perform webdriver specific actions before actually starting the test.
     *
     * @param driver AutomationDriver to initialize
     * @throws Exception If AutomationDriver failed to initialize
     */
    protected void initializeDriver(AutomationDriver driver) throws Exception {
        // Perform webdriver specific initialisation, eg navigate to webpage or dismiss updates in app
    }

    /**
     * Abstract method that must always be implemented to specify what webdriver that should be used.
     *
     * @return driver config id.
     */
    protected abstract String getDriverConfigId();

    /**
     * Abstract method that must be implemented to specify the path to the config xml file.
     *
     * @return path to property file.
     */
    protected abstract String getConfigFile();

    /**
     * Abstract method that must be implemented for initialization of the page objects.
     *
     * @param driver AutomationDriver to initialize page objects
     */
    protected abstract void initPages(AutomationDriver driver);

    protected void goToPageURL(AutomationDriver driver) {
        if (!BaseTestConfig.getConfigurationOption("mainpage.url").equalsIgnoreCase("")) {
            driver.navigate().to(BaseTestConfig.getConfigurationOption("mainpage.url"));
        }
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
     * Load mocked data based on category and data name.
     *
     * @param driver             AutomationDriver to load mocked data
     * @param mockedDataCategory The category of mocked data
     * @param mockedDataName     The name of the mocked data
     * @throws Exception         If AutomationDriver failed
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
     * @param driver AutomationDriver used to perform actions before loading mocked data
     */
    protected void performBeforeLoadingMockedData(AutomationDriver driver) {
    }

    /**
     * Runs after loading the mocked data. This method can be overloaded if actions related to mocked data
     * is needed after loading the mocked data.
     *
     * @param driver AutomationDriver used to perform actions after loading mocked data
     */
    protected void performAfterLoadingMockedData(AutomationDriver driver) {
    }

}
