package se.soprasteria.automatedtesting.webdriver.helpers.base.basetestlistener;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class BTLHelper {

    public static void savePageSource(Logger logger, ITestResult result) {
        try {
            AutomationDriver driver = (AutomationDriver)result.getParameters()[0];
            savePageSource(logger, driver, result.getName() + ".html");
        } catch (Exception e) {
            logger.error("Failed to write pagesource to file");
        }
    }

    public static void savePageSource(Logger logger, AutomationDriver driver, String filename) {
        try {
            String path = "target" + File.separator + "surefire-reports" + File.separator + filename;
            FileUtils.writeStringToFile (new File(path), driver.getPageSource(), Charset.defaultCharset());
        } catch (IOException e) {
            logger.error("Failed to write page source to file on failed test: " + e.getMessage());
        }
    }

    public static void cleanupWebDrivers(ITestResult result) {
        for (Object parameter: result.getParameters()) {
            if (parameter instanceof WebDriver) {
                ((WebDriver) parameter).quit();
            }
        }
    }
}
