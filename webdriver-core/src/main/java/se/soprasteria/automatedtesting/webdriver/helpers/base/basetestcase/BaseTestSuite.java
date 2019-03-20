package se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase;

import com.saucelabs.saucerest.SauceREST;
import io.github.bonigarcia.wdm.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import se.soprasteria.automatedtesting.webdriver.api.utility.Data;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig.Capability;

import java.io.File;
import java.util.List;

/**
 *
 * This class prepares the testing environment before running a testsuite
 */
public class BaseTestSuite {
    private static final Logger logger = LogManager.getLogger(BaseTestSuite.class);

    public static void initializeRuntimeEnvironment(List<DriverConfig> driverConfigurations) {
        doInitialization(driverConfigurations);
    }

    private static void doInitialization(List<DriverConfig> driverConfigurations) {
        logger.info("Initializing testsuite, should only run once per");
        for (DriverConfig driverConfiguration: driverConfigurations) {

            if (driverConfiguration.version != null) {
                initialiseDriverBinary(driverConfiguration.type, driverConfiguration.version);
            } else {
                initialiseDriverBinary(driverConfiguration.type);
            }

            if (driverConfiguration.runtimeEnvironment != null) {
                if (driverConfiguration.runtimeEnvironment.contentEquals("saucelabs")) {
                    initializeSauceLabsEnvironment(driverConfiguration);
                }
            }
        }
    }

    private static void initialiseDriverBinary(String type, String version) {
        if (!version.contentEquals("")) logger.info(String.format("Attempting to use version %s for webdriver %s.", version, type));
        else logger.info(String.format("Attempting to use the latest available version for webdriver %s.", type));
        switch (type) {
            case "FirefoxDriver":
                try {
                    FirefoxDriverManager.getInstance(FirefoxDriver.class).version(version).setup(); break;
                }
                catch (RuntimeException re) {
                    sendWebDriverVersionNotFoundMessage(type, version);
                    FirefoxDriverManager.getInstance(FirefoxDriver.class).version("").setup(); break;
                }
            case "ChromeDriver":
                try {
                    ChromeDriverManager.getInstance(ChromeDriver.class).version(version).setup(); break;
                }
                catch (RuntimeException re) {
                    sendWebDriverVersionNotFoundMessage(type, version);
                    ChromeDriverManager.getInstance(ChromeDriver.class).version("").setup(); break;
                }
            case "InternetExplorerDriver":
                try {
                    InternetExplorerDriverManager.getInstance(InternetExplorerDriver.class).arch32().version(version).setup(); break;
                }
                catch (RuntimeException re) {
                    sendWebDriverVersionNotFoundMessage(type, version);
                    InternetExplorerDriverManager.getInstance(InternetExplorerDriver.class).arch32().version("").setup(); break;
                }
            case "OperaDriver":
                try {
                    OperaDriverManager.getInstance(OperaDriver.class).version(version).setup(); break;
                }
                catch (RuntimeException re) {
                    sendWebDriverVersionNotFoundMessage(type, version);
                    OperaDriverManager.getInstance(OperaDriver.class).version("").setup(); break;
                }
            case "EdgeDriver":
                try {
                    EdgeDriverManager.getInstance(EdgeDriver.class).version(version).setup(); break;
                }
                catch (RuntimeException re) {
                    sendWebDriverVersionNotFoundMessage(type, version);
                    EdgeDriverManager.getInstance(EdgeDriver.class).version("").setup(); break;
                }
            case "WindowsDriver":
                try {
                    logger.info("To use the Windows Driver you must first manually install the latest version of the WAD (WinAppDriver): https://github.com/Microsoft/WinAppDriver/releases");
                }
                catch (Exception re) {

                }
        }
    }

    private static void initialiseDriverBinary(String type) {
        initialiseDriverBinary(type, "");
    }

    private static void initializeSauceLabsEnvironment(DriverConfig driverConfiguration) {
        try {
            logger.info("uploading app to saucelabs");
            String userNameAndKeyRegex = "http://(\\S+):([a-zA-Z0-9-]+)@.*";
            List<String> parsedSauceUrl = Data.stringGetGroups(driverConfiguration.url, userNameAndKeyRegex);
            String username = parsedSauceUrl.get(1);
            String accessKey = parsedSauceUrl.get(2);
            SauceREST sauceREST = new SauceREST(username, accessKey);

            Capability appCapability = getAppCapability(driverConfiguration);
            File app = new File(appCapability.getValue());
            String remoteFileName = appCapability.alternateValue.split(":")[1];
            logger.info("uploading app [local path,remote path]: [" + appCapability.getValue() + "," + remoteFileName + "]");
            sauceREST.uploadFile(app, remoteFileName, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload app to saucelabs");
        }
    }

    private static Capability getAppCapability(DriverConfig driverConfiguration) {
        for (Capability capability: driverConfiguration.capabilities) {
            if (capability.name.contentEquals("app")) {
                return capability;
            }
        }
        throw new RuntimeException("No app found, can not proceed");
    }

    private static void sendWebDriverVersionNotFoundMessage(String type, String version) {
        logger.warn(String.format("Running %s with latest version instead, because %s wasn't found.", type, version));
    }
}
