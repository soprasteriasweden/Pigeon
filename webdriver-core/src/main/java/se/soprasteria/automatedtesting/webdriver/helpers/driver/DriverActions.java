package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import com.google.common.base.Stopwatch;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.*;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DriverActions extends BaseClass {

    private final AutomationDriver driver;
    private final int ORIGINAL_IMPLICIT_TIMEOUT;
    private final String ORIGINAL_IMPLICIT_TIMEOUT_KEY = "webdriver.implicit.timeout";
    private final String CURRENT_IMPLICIT_TIMEOUT_KEY = "webdriver.implicit.timeout.current";
    private final String CURRENT_IMPLICIT_TIMEOUT_ENABLED_KEY = "webdriver.implicit.timeout.enabled";

    public DriverActions(AutomationDriver driver) {
        this.driver = driver;
        ORIGINAL_IMPLICIT_TIMEOUT = Integer.parseInt(System.getProperty(ORIGINAL_IMPLICIT_TIMEOUT_KEY));

        if (System.getProperty(CURRENT_IMPLICIT_TIMEOUT_KEY) == null) {
            System.setProperty(CURRENT_IMPLICIT_TIMEOUT_KEY, System.getProperty(ORIGINAL_IMPLICIT_TIMEOUT_KEY));
        }
    }

    public void restoreOriginalImplicitTimeout() {
        setImplicitTimeout(ORIGINAL_IMPLICIT_TIMEOUT);
    }

    public void setImplicitTimeout(int millis) {
        System.setProperty(CURRENT_IMPLICIT_TIMEOUT_KEY, Integer.toString(millis));
        driver.manage().timeouts().implicitlyWait(millis, TimeUnit.MILLISECONDS);
    }

    public int getImplicitTimeout() {
        String timeout = System.getProperty(CURRENT_IMPLICIT_TIMEOUT_KEY);
        if (timeout == null) {
            restoreOriginalImplicitTimeout();
            return ORIGINAL_IMPLICIT_TIMEOUT;
        }
        return Integer.parseInt(timeout);
    }

    public void disableImplicitTimeout() {
        if (ORIGINAL_IMPLICIT_TIMEOUT > 0 && isImplicitTimeoutEnabled()) {
            System.setProperty(CURRENT_IMPLICIT_TIMEOUT_ENABLED_KEY, "false");
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        }
    }

    public void enableImplicitTimeout() {
        if (ORIGINAL_IMPLICIT_TIMEOUT > 0 && !isImplicitTimeoutEnabled()) {
            System.setProperty(CURRENT_IMPLICIT_TIMEOUT_ENABLED_KEY, "true");
            setImplicitTimeout(getImplicitTimeout());
        }
    }

    private boolean isImplicitTimeoutEnabled() {
        String timeoutsEnabled = System.getProperty(CURRENT_IMPLICIT_TIMEOUT_ENABLED_KEY);
        if (timeoutsEnabled == null) {
            System.setProperty(CURRENT_IMPLICIT_TIMEOUT_ENABLED_KEY, "true");
            return true;
        }
        if (timeoutsEnabled.contains("true")) {
            return true;
        }
        return false;
    }

    public Object executeJavaScript(WebDriver webDriver, String script) {
        try {
            return ((JavascriptExecutor)webDriver).executeScript(script);
        } catch (ClassCastException e) {
            logger.warn("Unsupported driver type for JavaScript execution");
            throw e;
        } catch (Exception e) {
            logger.warn("Could not perform JavaScript execution correctly, check your script structure and selenium compatibility");
            throw e;
        }
    }

    public Object executeJavaScript(WebDriver webDriver, String script, Object... args) {
        try {
            return ((JavascriptExecutor)webDriver).executeScript(script, args);
        } catch (ClassCastException e) {
            logger.warn("Unsupported driver type for JavaScript execution");
            throw e;
        } catch (Exception e) {
            logger.warn("Could not perform JavaScript execution correctly, check your script structure and selenium compatibility");
            throw e;
        }
    }

    public void waitForAngularRequestToFinish(WebDriver webDriver) {
        executeAngularWait(createNgWebDriver(webDriver));
    }

    public void waitForAngularRequestToFinish(WebDriver webDriver, String rootSelector) {
        executeAngularWait(createNgWebDriver(webDriver).withRootSelector(rootSelector));
    }

    private NgWebDriver createNgWebDriver(WebDriver webDriver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor)webDriver;
            return new NgWebDriver(js);
        } catch(ClassCastException e) {
            logger.warn("Unsupported driver type for Angular async activity wait");
            throw e;
        }
    }

    private void executeAngularWait(NgWebDriver ngWebDriver) {
        if(driver.isIos()) logger.info("IOSDriver detected, waitForAngularRequestToFinish " +
                "may stall due to a compatibility issue with XCUITest. It is advised not to use waitForAngularRequestToFinish " +
                "with IOSDriver for the time being.");
        try {
            disableImplicitTimeout();
            final Stopwatch stopwatch = Stopwatch.createStarted();
            ngWebDriver.waitForAngularRequestsToFinish();
            logger.trace("Finished Angular async activity wait, elapsed time: " + stopwatch);
            stopwatch.reset();
            enableImplicitTimeout();
        } catch (Exception e) {
            logger.warn("Could not wait for Angular to finish async activity");
            throw e;
        }
    }

    public void waitAndKeepSessionActive(int millisWait, int millisRefreshInterval) {
        if(driver.isMobile() && !driver.getMobileDriver().isBrowser()) {
            throw new IllegalArgumentException("Method not compatible with native context, use waitAndKeepSessionActive taking elements parameters instead");
        }
        if(millisWait <= 0 || millisRefreshInterval <= 0 || millisRefreshInterval >= millisWait) {
            throw new IllegalArgumentException("Wait time or interval incorrectly set, must be greater than zero and wait time must be greater than interval");
        }
        long startWait = System.currentTimeMillis();
        logger.info("Waiting " + millisToMinAndSec(millisWait) + ", refreshing every " + millisToMinAndSec(millisRefreshInterval) + " to keep current session active");
        while(System.currentTimeMillis()-startWait < millisWait){
            sleep(millisRefreshInterval);
            driver.navigate().refresh();
        }
    }

    public void waitAndKeepSessionActive(int millisWait, int millisClickInterval, WebElement webElement) {
        if(millisWait <= 0 || millisClickInterval <= 0 || millisClickInterval >= millisWait) {
            throw new IllegalArgumentException("Wait time or interval incorrectly set, must be greater than zero and wait time must be greater than interval");
        }
        long startWait = System.currentTimeMillis();
        logger.info("Waiting " + millisToMinAndSec(millisWait) + ", clicking WebElement every " + millisToMinAndSec(millisClickInterval) + " to keep current session active");
        while(System.currentTimeMillis()-startWait < millisWait){
            sleep(millisClickInterval);
            webElement.click();
        }
    }

    public void waitAndKeepSessionActive(int millisWait, int millisClickInterval, WebElement firstWebElement, WebElement secondWebElement) {
        if(millisWait <= 0 || millisClickInterval <= 0 || millisClickInterval >= millisWait) {
            throw new IllegalArgumentException("Wait time or interval incorrectly set, must be greater than zero and wait time must be greater than interval");
        }
        long startWait = System.currentTimeMillis();
        logger.info("Waiting " + millisToMinAndSec(millisWait) + ", clicking elements every " + millisToMinAndSec(millisClickInterval) + " to keep current session active");
        while(System.currentTimeMillis()-startWait < millisWait){
            sleep(millisClickInterval);
            firstWebElement.click();
            sleep(millisClickInterval);
            secondWebElement.click();
        }
    }

    private static String millisToMinAndSec(int millis) {
        String timeToWait = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        if(minutes > 0 && seconds > 0) timeToWait = String.format("%d min, %d sec", minutes, seconds);
        else if (minutes > 0) timeToWait = String.format("%d min", minutes);
        else if (seconds > 0) timeToWait = String.format("%d sec", seconds);
        return timeToWait;
    }

    public static Set<String> getAvailableContexts(WebDriver webDriver, int timeOutMillis) {
        try {
            Set<String> contextHandles = ((AppiumDriver)webDriver).getContextHandles();
            return contextHandles;
        } catch (WebDriverException|NullPointerException expected) {
            return retryContextGetter(webDriver, timeOutMillis);
        } catch (ClassCastException cce) {
            throw new WebDriverException("Can only get available contexts on Android, iOS or Windows drivers.");
        }
    }

    private static Set<String> retryContextGetter(WebDriver webDriver, int timeOutMillis) {
        Set<String> contextHandles;
        final Stopwatch stopwatch = Stopwatch.createStarted();
        while(true) {
            try {
                contextHandles = ((AppiumDriver)webDriver).getContextHandles();
                if(contextHandles != null && !contextHandles.contains(null)) break;
            } catch (WebDriverException|NullPointerException expected) {
                if(stopwatch.elapsed(TimeUnit.MILLISECONDS) >= timeOutMillis) throw expected;
            }
        }
        return contextHandles;
    }

    public boolean isAllHrefResponding(WebDriver driver, String tagName, String attribute) {
        boolean tagNamesCorrect = false;
        String url;
        int respCode;
        String[] schemes = {"http", "https", "mailto", "ftp"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        HttpURLConnection httpURLConnection;
        List<WebElement> links = driver.findElements(By.tagName(tagName));
        Iterator<WebElement> iterator = links.iterator();

        while (iterator.hasNext()) {
            url = iterator.next().getAttribute(attribute);
            if (urlValidator.isValid(url)) {
                try {
                    httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
                    httpURLConnection.setRequestMethod("HEAD");
                    httpURLConnection.connect();
                    respCode = httpURLConnection.getResponseCode();
                    if (respCode >= 400) {
                        logger.error("URL is broken: " + url);
                        tagNamesCorrect = true;
                        continue;
                    }
                    logger.info(url);
                } catch (MalformedURLException e) {
                    logger.error(e);
                } catch (IOException e) {
                    logger.error(e);
                }
            } else if (url == null || url.isEmpty()) {
                logger.error("URL is empty!");
                tagNamesCorrect = true;
            } else if (url.startsWith("mailto")) {
                logger.info("URL for mail: " + url);
            } else {
                logger.error("URL is invalid: " + url);
                tagNamesCorrect = true;
            }
        }
        return tagNamesCorrect;
    }

    public boolean isListTagNameValue(WebDriver webDriver, String tagName) throws IOException {

        String getUrl = readStringFromURL(webDriver.getCurrentUrl());
        List<String> resultGetTagValue = getTagValues(getUrl, tagName);
        for (String names : resultGetTagValue) {
            if (names != null && !names.isEmpty()) {
                logger.info("List all tag elements : " + names);
                return false;
            }
        }
        return true;
    }

    public List<String> getTagValues(String url, String tagName) {
        String inputTagName = "<" + tagName + "[^>]*>(.*?)</" + tagName + ">";
        Pattern TAG_REGEX = Pattern.compile(inputTagName);
        List<String> tagValues = new ArrayList<>();
        Matcher matcher = TAG_REGEX.matcher(url);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }

    public String readStringFromURL(String requestURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
