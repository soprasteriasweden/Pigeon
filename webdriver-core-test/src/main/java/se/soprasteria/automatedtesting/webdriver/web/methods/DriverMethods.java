package se.soprasteria.automatedtesting.webdriver.web.methods;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.WebDriverType;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.model.pages.MainPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class DriverMethods extends MainPage {

    public DriverMethods(AutomationDriver driver) {
        super(driver);
    }

    public boolean didScreenChange() {
        return driver.didScreenChangeDuringInterval(2000);
    }

    public boolean didScreenChangeMoreThanAllowedThreshold() {
        return driver.didScreenChangeDuringInterval(75, 2000);
    }

    public void waitAndKeepSessionActive() {
        driver.waitAndKeepSessionActive(15000, 2000);
    }

    public void waitAndKeepSessionActiveOneElement() {
        driver.waitAndKeepSessionActive(15000, 2000, mainPageLink);
    }

    public void waitAndKeepSessionActiveTwoElements() {
        driver.waitAndKeepSessionActive(15000, 2000, absolutePositionPageLink, mainPageLink);
    }

    public void savePageSource() {
        driver.writeSourceToFile(logger, driver, pageSourceFileName);
        logger.info("Saved image.");
    }

    public boolean validatePageSourceExistence() {
        File pageSource = new File("target/surefire-reports/" + pageSourceFileName);
        return pageSource.exists() && !pageSource.isDirectory();
    }

    public boolean validatePageSourceContent() {
        boolean bodyOpenerExist = false;
        boolean bodyCloserExist = false;
        boolean sideNavBarExist = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("target/surefire-reports/" + pageSourceFileName));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                if(!bodyOpenerExist && line.contains("<body>")) bodyOpenerExist = true;
                if(!bodyCloserExist && line.contains("</body>")) bodyCloserExist = true;
                if(!sideNavBarExist && line.contains("<h1 class=\"page-header\">Main</h1>")) sideNavBarExist = true;
            }
            bufferedReader.close();
        } catch (IOException e) {
            return false;
        }
        return bodyOpenerExist && bodyCloserExist && sideNavBarExist;
    }

    public void deletePageSourceFile() {
        new File("target/surefire-reports/" + pageSourceFileName).delete();
    }

    public boolean isPlatform(WebDriverType type) {
        return driver.isPlatform(type);
    }

    public WebDriverType getWebDriverType() {
        return driver.getWebDriverType();
    }

    public String getWebDriverName() {
        return driver.getWebDriverName();
    }

    public boolean isMobile() {
        return driver.isMobile();
    }

    public boolean isWeb() {
        return driver.isWeb();
    }

    public boolean isChrome() {
        return driver.isChrome();
    }

    public boolean isFirefox() {
        return driver.isFirefox();
    }

    public boolean isSafari() {
        return driver.isSafari();
    }

    public boolean isExplorer() {
        return driver.isInternetExplorer();
    }

    public boolean isEdge() {
        return driver.isEdge();
    }

    public boolean isOpera() { return driver.isOpera(); }

    public boolean isWindowsDriver() {
        return driver.isWindowsDriver();
    }

    public boolean isIos() {
        return driver.isIos();
    }

    public boolean isAndroid() {
        return driver.isAndroid();
    }

    public boolean isRemoteWebDriver() {
        return driver.isRemoteWebDriver();
    }

    public void clickShowElementWithJavascript() {
        driver.executeJavaScript("arguments[0].click();", showClickableElementButton);
    }

    public int getImplicitTimeout() {
        return driver.getImplicitTimeout();
    }

    public void setImplicitTimeout(int timeout) {
        driver.setImplicitTimeout(timeout);
    }

    public void resetImplicitTimeout() {
        driver.resetTimeout();
    }

    public String getCapability(String capabilityName) {
        return driver.getCapability(capabilityName);
    }

    public boolean correctContextsAvailable() {
        if(driver.isMobile()) {
            Set<String> availableContexts = driver.getAvailableContexts(10000);
            for(String context : availableContexts) {
                String contextLower = context.toLowerCase();
                if(!contextLower.contains("native") &&
                        !contextLower.contains("webview") &&
                        !contextLower.contains("chromium") &&
                        !contextLower.contains("safari")) {
                    logger.error("Incorrect context available: " + context);
                    return false;
                }
            }
        } else {
            try {
                driver.getAvailableContexts(10000);
                return false;
            } catch (Exception expected) { }
        }
        return true;
    }

    public boolean doesDebugLevelParameterContainDriverLog() {
        return BaseTestConfig.getConfigurationOption("debugLevel").contains("DRIVERLOG");
    }

    public boolean isDriverLoggingEnabled() {
        return driver.isDriverLoggingEnabled();
    }

    public boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
