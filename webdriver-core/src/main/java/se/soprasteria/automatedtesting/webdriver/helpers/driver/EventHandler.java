package se.soprasteria.automatedtesting.webdriver.helpers.driver;

import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import se.soprasteria.automatedtesting.webdriver.api.datastructures.DebugLevel;
import se.soprasteria.automatedtesting.webdriver.api.utility.DebugImage;
import se.soprasteria.automatedtesting.webdriver.api.utility.QAColors;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.session.Session;

import java.io.IOException;


/**
 *
 * Conveniently catches the event for better logs. The eventhandler is specified when setting up the webdriver as
 * an EventFiringWebDriver in the Setup class.
 */

class EventHandler implements AppiumWebDriverEventListener {
    private final Logger logger;
    private By lastByTheTestSearched;
    private String testmethod;

    public EventHandler(String testmethod) {
        this.logger = LogManager.getLogger(EventHandler.class);
        this.testmethod = testmethod;
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        String elementString = element.toString();
        elementString = elementString.substring(elementString.lastIndexOf("->") + 3, elementString.length() - 1);
        logger.trace("Clicked element: (" + elementString + ")");
        WebDriverLog.printLogFile(logger);
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {

    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {

    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver){
        if(by != lastByTheTestSearched) {
            logger.trace("Finding element: (" + by + ")");
            lastByTheTestSearched = by;
        }
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver){
        WebDriverLog.printLogFile(logger);
    }

    @Override
    public void onException(Throwable error, WebDriver driver){
        WebDriverLog.printLogFile(logger);
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {

    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {

    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {

    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        if (currentLevel == DebugLevel.Level.IMAGES || currentLevel == DebugLevel.Level.IMAGES_CLICKS ) {
            try {
                Locatable location = (Locatable) element;
                int x = location.getCoordinates().inViewPort().getX();
                int y = location.getCoordinates().inViewPort().getY();
                DebugImage.appendImage(logger,
                        Session.getTestMethodID(null),
                        Screenshot.captureScreenshot(),
                        "BEFORE CLICK: " + element.getText() + " (" + element.getTagName() + ")",
                        QAColors.getColor(QAColors.State.NEUTRAL),
                        x,
                        y,
                        element.getSize().width,
                        element.getSize().height);
            } catch (IOException e) {
                logger.error("Failed to capture screenshot");
            }
        }
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        if (currentLevel == DebugLevel.Level.IMAGES) {
            try {
                Locatable location = (Locatable) element;
                int x = location.getCoordinates().inViewPort().getX();
                int y = location.getCoordinates().inViewPort().getY();
                DebugImage.appendImage(logger,
                        Session.getTestMethodID(null),
                        Screenshot.captureScreenshot(),
                        "BEFORE EDIT: " + element.getText() + " (" + element.getTagName() + ")",
                        QAColors.getColor(QAColors.State.NEUTRAL),
                        x,
                        y,
                        element.getSize().width,
                        element.getSize().height);
            } catch (IOException e) {
                logger.error("Failed to capture screenshot");
            }
        }
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) {
        
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        if (currentLevel == DebugLevel.Level.IMAGES) {
            try {
                DebugImage.appendImage(logger,
                        Session.getTestMethodID(null),
                        Screenshot.captureScreenshot(),
                        "BEFORE SCRIPT: " + script.substring(0,40),
                        QAColors.getColor(QAColors.State.NEUTRAL));
            } catch (IOException e) {
                logger.error("Failed to capture screenshot");
            }
        }
    }

    @Override
    public void afterScript(String s, WebDriver webDriver) {

    }

    @Override
    public void beforeSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void afterSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {

    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {

    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {

    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateTo(String s, WebDriver webDriver) {

    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        logger.info("Driver loaded URL: " + url);
        DebugLevel.Level currentLevel = DebugLevel.getLevel();
        if (currentLevel == DebugLevel.Level.IMAGES ||
                currentLevel == DebugLevel.Level.IMAGES_CLICKS ||
                currentLevel == DebugLevel.Level.IMAGES_PAGES) {
            try {
                DebugImage.appendImage(logger,
                        Session.getTestMethodID(null),
                        Screenshot.captureScreenshot(),
                        "AFTER OPEN: " + url,
                        QAColors.getColor(QAColors.State.NEUTRAL));
            } catch (IOException e) {
                logger.error("Failed to capture screenshot");
            }
        }
    }

    @Override
    public void beforeNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {

    }

}