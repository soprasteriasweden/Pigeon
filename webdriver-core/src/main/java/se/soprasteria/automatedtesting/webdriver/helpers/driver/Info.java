package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.WebDriverType;

/**
 *
 * Returns info on webdrivers
 */
public class Info {

    public static boolean isMobile(WebDriver webDriver) {
        return isPlatform(webDriver, WebDriverType.AndroidDriver)
                || isPlatform(webDriver, WebDriverType.IOSDriver);
    }

    public static boolean isMobile(WebDriverType webDriverType) {
        return isAndroid(webDriverType) || isIos(webDriverType);
    }

    public static boolean isWeb(WebDriver webDriver) {
        return !isMobile(webDriver) && !isWindows(webDriver);
    }

    public static boolean isWeb(WebDriverType webDriverType) {
        return !isMobile(webDriverType) && !isWindows(webDriverType);
    }

    public static boolean isWindows(WebDriver webDriver) {
        return isPlatform(webDriver, WebDriverType.WindowsDriver);
    }

    public static boolean isWindows(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.WindowsDriver;
    }

    public static boolean isAndroid(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof AndroidDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof AndroidDriver;
        }
    }

    public static boolean isAndroid(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.AndroidDriver;
    }

    public static boolean isIos(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof IOSDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof IOSDriver;
        }
    }

    public static boolean isIos(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.IOSDriver;
    }

    public static boolean isChrome(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof ChromeDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof ChromeDriver;
        }
    }

    public static boolean isChrome(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.ChromeDriver;
    }

    public static boolean isRemoteWebDriver(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof RemoteWebDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof RemoteWebDriver;
        }
    }

    public static boolean isRemoteWebDriver(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.RemoteWebDriver;
    }

    public static boolean isFirefox(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof FirefoxDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof FirefoxDriver;
        }
    }

    public static boolean isFirefox(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.FirefoxDriver;
    }

    public static boolean isInternetExplorer(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof InternetExplorerDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof InternetExplorerDriver;
        }
    }

    public static boolean isInternetExplorer(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.InternetExplorerDriver;
    }

    public static boolean isEdge(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof EdgeDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof EdgeDriver;
        }
    }

    public static boolean isEdge(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.EdgeDriver;
    }

    public static boolean isSafari(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof SafariDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof SafariDriver;
        }
    }

    public static boolean isSafari(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.SafariDriver;
    }

    public static boolean isOpera(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof OperaDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof OperaDriver;
        }
    }

    public static boolean isOpera(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.OperaDriver;
    }

    public static boolean isHtmlUnit(WebDriver webDriver) {
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            return evf.getWrappedDriver() instanceof HtmlUnitDriver;
        } catch (ClassCastException cce) {
            return webDriver instanceof HtmlUnitDriver;
        }
    }

    public static boolean isHtmlUnit(WebDriverType webDriverType) {
        return webDriverType == WebDriverType.HtmlUnitDriver;
    }

    public static boolean isPlatform(WebDriver webDriver, WebDriverType webDriverType) {
        boolean check;
        try {
            EventFiringWebDriver evf = (EventFiringWebDriver) webDriver;
            check =  evf.getWrappedDriver().getClass().getSimpleName().equals(webDriverType.name());
        } catch (ClassCastException cce) {
            check = webDriver.getClass().getSimpleName().contains(webDriverType.name());
        }

        return check;
    }

    public static WebDriverType getWebDriverType(WebDriver webDriver) {
        if (isPlatform(webDriver, WebDriverType.AndroidDriver)) {
            return WebDriverType.AndroidDriver;
        } else if (isPlatform(webDriver, WebDriverType.IOSDriver)) {
            return WebDriverType.IOSDriver;
        } else if (isPlatform(webDriver, WebDriverType.ChromeDriver)) {
            return WebDriverType.ChromeDriver;
        } else if (isPlatform(webDriver, WebDriverType.InternetExplorerDriver)) {
            return WebDriverType.InternetExplorerDriver;
        } else if (isPlatform(webDriver, WebDriverType.FirefoxDriver)) {
            return WebDriverType.FirefoxDriver;
        } else if (isPlatform(webDriver, WebDriverType.SafariDriver)) {
            return WebDriverType.SafariDriver;
        } else if (isPlatform(webDriver, WebDriverType.EdgeDriver)) {
            return WebDriverType.EdgeDriver;
        } else if (isPlatform(webDriver, WebDriverType.WindowsDriver)) {
            return WebDriverType.WindowsDriver;
        } else if(isPlatform(webDriver, WebDriverType.MarionetteDriver)) {
            return WebDriverType.MarionetteDriver;
        } else if (isPlatform(webDriver, WebDriverType.OperaDriver)) {
            return WebDriverType.OperaDriver;
        } else if(isPlatform(webDriver, WebDriverType.HtmlUnitDriver)) {
            return WebDriverType.HtmlUnitDriver;
        }else {
            throw new RuntimeException("Unknown WebDriver type");
        }
    }

    public static String getWebDriverName(WebDriver webDriver){
        try {
            return getWebDriverType(webDriver).toString();
        } catch (Exception e) {
            throw new RuntimeException("Unknown WebDriver type, cannot get driver name");
        }

    }

}
