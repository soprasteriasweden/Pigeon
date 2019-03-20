package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.DriverActions;
import se.soprasteria.automatedtesting.webdriver.helpers.element.Comparison;
import se.soprasteria.automatedtesting.webdriver.helpers.element.ElementScreenshot;
import se.soprasteria.automatedtesting.webdriver.helpers.element.Interaction;
import se.soprasteria.automatedtesting.webdriver.helpers.element.Presence;

import java.io.File;
import java.util.List;

/**
 * Helpers to streamline working with Selenium WebElements. All time units are milliseconds unless something else is
 * specified. These helper functions provide the following functionality:
 * - wrap wait functionality so that the explicit waits it works well with implicit waits
 * - wraps waits into booleans
 * - wraps findElements into booleans in isPresent...
 * - centralized debuglogging
 */
public class ElementHelper {
    private AutomationDriver driver;
    private Presence presence;
    private Interaction interaction;
    private Comparison comparison;
    private ElementScreenshot elementScreenshot;
    private DriverActions driverActions;
    private Logger logger;

    private final int DEFAULT_TIMEOUT_MILLIS = 2000;

    public ElementHelper(Logger logger, AutomationDriver driver) {
        this.driver = driver;
        this.logger = logger;
        this.presence = new Presence(this.driver);
        this.interaction = new Interaction(this.driver);
        this.comparison = new Comparison(this.driver);
        this.elementScreenshot = new ElementScreenshot(this.driver);
        this.driverActions = new DriverActions(this.driver);
    }

    /**
     * Wait for the element to be clickable and then click it. If the element isn't clickable within the specified time
     * an assertionerror is thrown.
     *
     * @param webElement    element to click
     * @param timeoutMillis timeout before throwing the assertionerrors. Set to -1 to use current implicit timeoutMillis value.
     * @param errorMessage  message to show if the element isn't clicked in time
     *
     * @throws AssertionError
     */
    public void clickWithinTime(WebElement webElement, int timeoutMillis, String errorMessage) throws AssertionError {
        if (isElementClickableWithinTime(webElement, timeoutMillis)) {
            webElement.click();

        } else {
            throw new AssertionError(errorMessage);
        }
    }


    /**
     * Wait for the element to be clickable and then click it. If the element isn't clickable within the specified time
     * no assertionerror is thrown, execution just continues
     *
     * @param webElement    WebElement to click
     * @param timeoutMillis timeout before throwing the assertionerrors. Set to -1 to use current implicit timeoutMillis value.
     * @return boolean specifying if a click occured
     */
    public boolean clickWithinTime(WebElement webElement, int timeoutMillis) {
        if (isElementClickableWithinTime(webElement, timeoutMillis)) {
            webElement.click();
            return true;

        }

        return false;

    }

    /**
     * Check if element is displayed IF you don't know if the element is currently displayed OR if you expect
     * the element not to be present. If you are sure the element is currently displayed use webElement.isDisplayed()
     * instead.
     *
     * @param webElement WebElement to inspect
     * @return true if the element was present and displayed, false if it wasn't.
     */
    public boolean isElementPresentAndDisplayed(WebElement webElement) {
        return presence.isElementPresentAndDisplayed(webElement);
    }

    /**
     * Check if the element is present in the DOM and displayed, use IF you don't know if the element is currently displayed AND if you expect
     * the element not to be present. If you are sure the element is currently present in the DOM use
     * isElementDisplayedWithinTime(webElement, timeoutMillis) instead.
     *
     * @param webElement    WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was present and displayed before timeoutMillis ran out, false if it wasn't.
     */
    public boolean isElementPresentAndDisplayedWithinTime(WebElement webElement, int timeoutMillis) {
        return presence.isElementPresentAndDisplayedWithinTime(webElement, timeoutMillis);
    }

    /**
     * Wait for element to be present in the DOM and displayed, use IF you don't know if the element is currently displayed AND if you expect
     * the element not to be present. If you are sure the element is currently present in the DOM use
     * waitForElementToBeDisplayed(webElement, timeoutMillis) instead.
     *
     * @param webElement    WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBePresentAndDisplayed(WebElement webElement, int timeoutMillis) {
        presence.waitForElementToBePresentAndDisplayed(webElement, timeoutMillis);
    }

    /**
     * Check if the element is clickable within the time frame. Clickable means
     * the element is enabled, visible on screen and that it's not covered by any other elements.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was clickable before timeoutMillis ran out, false if it wasn't.
     */
    public boolean isElementClickableWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementClickableWithinTime(element, timeoutMillis);
    }

    /**
     * Check if the element is displayed within the time frame. Displayed means
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was displayed before the timeoutMillis ran out, false if it wasn't.
     */
    public boolean isElementDisplayedWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementDisplayedWithinTime(element, timeoutMillis);
    }

    /**
     * Check if the element is displayed within the time frame. Displayed means
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was displayed before the timeoutMillis ran out, false if it wasnt.
     */
    public boolean isElementDisplayedWithinTime(By by, int timeoutMillis) {
        return presence.isElementDisplayedWithinTime(by, timeoutMillis);
    }


    /**
     * Check for an element to no longer be displayed within the time frame.
     * It will continually poll until timeout is exceeded for as long as element is displayed.
     * Will return true if element is no longer displayed (or wasn't to begin with).
     *
     * @param element       WebElement to search for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was no longer present in the DOM before the timeoutMillis ran out, false if it still was.
     */
    public boolean isElementGoneWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementGoneWithinTime(element, timeoutMillis);
    }

    /**
     * Wait for an element to no longer be present in the DOM within the time frame.
     *
     * @param element       WebElement to wait for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeGone(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeGone(element, timeoutMillis);
    }

    /**
     * Wait for an element to either be hidden or no longer be present in the DOM within the time frame.
     *
     * @param element       WebElement to wait for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeHiddenOrGone(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeHiddenOrGone(element, timeoutMillis);
    }

    /**
     * Check if the element is hidden within the specified time. Hidden means the element is
     * not visible on screen but present in the DOM.
     *
     * @param element       WebElement to search for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if element is invisible (ie with the 'hidden' tag)
     */
    @Beta
    public boolean isElementHiddenWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementHiddenWithinTime(element, timeoutMillis);
    }

    /**
     * Check if element is either hidden or no longer be present in the DOM within the time frame.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if element is invisible or if element is not present in DOM before timeoutMillis ran out
     */
    @Beta
    public boolean isElementHiddenOrGoneWithinTime(By by, int timeoutMillis) {
        return presence.isElementHiddenOrGoneWithinTime(by, timeoutMillis);
    }

    /**
     * Wait for an element to either be hidden or no longer be present in the DOM within the time frame.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeHiddenOrGone(By by, int timeoutMillis) {
        presence.waitForElementToBeHiddenOrGone(by, timeoutMillis);
    }

    /**
     * Check if element is either hidden or no longer be present in the DOM within the time frame.
     *
     * @param element       WebElement to search for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if element is invisible or if element is not present in DOM before timeoutMillis ran out
     */
    @Beta
    public boolean isElementHiddenOrGoneWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementHiddenOrGoneWithinTime(element, timeoutMillis);
    }

    /**
     * Check is element is present in the DOM within the time frame. The element can be present
     * in the DOM without being visible or displayed on the page.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if element is present in the DOM before timeoutMillis ran out, false if it is not present
     */
    public boolean isElementPresentWithinTime(By by, int timeoutMillis) {
        return presence.isElementPresentWithinTime(by, timeoutMillis);
    }

    /**
     * Wait for element to be present in the DOM within the time frame. The element can be present
     * in the DOM without being visible or displayed on the page.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBePresent(By by, int timeoutMillis) {
        presence.waitForElementToBePresent(by, timeoutMillis);
    }

    /**
     * Check if the text is present in the element within the time frame.
     *
     * @param element       element to inspect
     * @param text          text to search for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the text was present before timeoutMillis ran out, false if it was not present
     */
    public boolean isTextPresentInElementWithinTime(WebElement element, String text, int timeoutMillis) {
        return presence.isTextPresentWithinTime(element, text, timeoutMillis);
    }

    /**
     * Use element sendkeys with a controlled pause between each keypress. Usable for instance when entering keypresses
     * in dynamic searches (where you get suggestions as you enter more keypresses)
     *
     * @param element               WebElement to send keys to
     * @param searchString          String to search for
     * @param millisBetweenKeypress Milliseconds between keypresses
     */
    public void sendKeysWithControlledSpeed(WebElement element, String searchString, int millisBetweenKeypress) {
        interaction.sendKeysWithControlledSpeed(element, searchString, millisBetweenKeypress);
    }

    /**
     * Check if the element is clickable within the time frame. Clickable means the element is enabled, visible on
     * screen and that it's not covered by any other object.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeClickable(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeClickable(element, timeoutMillis);
    }

    /**
     * Check in an element is clickable and refreshed within the time frame. Use this method to prevent
     * finding stale elements which would trigger a StaleElementReferenceException.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was clickable before timeoutMillis ran out, false if it was not clickable
     */
    public boolean isElementClickableAndRefreshedWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementClickableAndRefreshedWithinTime(element, timeoutMillis);
    }

    /**
     * Wait for element to be clickable and refreshed within the time frame. Use this method to prevent
     * finding stale elements which would trigger a StaleElementReferenceException.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeClickableAndRefreshed(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeClickableAndRefreshed(element, timeoutMillis);
    }

    /**
     * Wait for element to be present in the DOM and displayed. Displayed means the element is visible on screen,
     * it does not have to be enabled.
     *
     * @param by            By to search for and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBePresentAndDisplayed(By by, int timeoutMillis) {
        presence.waitForElementToBePresentAndDisplayed(by, timeoutMillis);
    }

    /**
     * Wait for element to be displayed within the time frame. Displayed means the element is visible on screen,
     * it does not have to be enabled.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeDisplayed(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeDisplayed(element, timeoutMillis);
    }

    /**
     * Check if an element is displayed and refreshed within the time frame. Displayed means the element is
     * visible on screen, it does not have to be enabled. Use this method to prevent
     * finding stale elements which would trigger a StaleElementReferenceException.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was dusplayed before timeoutMillis ran out, false if it was not displayed
     */
    public boolean isElementDisplayedAndRefreshedWithinTime(WebElement element, int timeoutMillis) {
        return presence.isElementDisplayedAndRefreshedWithinTime(element, timeoutMillis);
    }

    /**
     * Wait for element to be displayed and refreshed within the time frame. Displayed means the element is
     * visible on screen, it does not have to be enabled. Use this method to prevent
     * finding stale elements which would trigger a StaleElementReferenceException.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeDisplayedAndRefreshed(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeDisplayedAndRefreshed(element, timeoutMillis);
    }

    /**
     * Wait for the element to be hidden within the specified time. Hidden means the element is
     * not visible on screen but present in the DOM.
     *
     * @param element       WebElement to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementToBeHidden(WebElement element, int timeoutMillis) {
        presence.waitForElementToBeHidden(element, timeoutMillis);
    }

    /**
     * Check if the text is present in the element within the time frame.
     *
     * @param element       WebElement to inspect
     * @param text          text to search for
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForTextToBePresentInElement(WebElement element, String text, int timeoutMillis) {
        presence.waitForTextToBePresentInElement(element, text, timeoutMillis);
    }

    /**
     * Get a screenshot of the provided WebElement
     *
     * @param element WebElement to screenshot
     * @return File with screenshot image
     */
    public File elementScreenshot(WebElement element) {
        return elementScreenshot.captureElementScreenshot(element);
    }

    /**
     * Do an image comparison of the element before and after the given interval. Returns false if identical.
     * When comparing elements visually the browser zoom level and windows/mac screen scaling must be at 100%
     *
     * @param element       WebElement to compare
     * @param timeoutMillis time interval to wait for element comparison
     * @return true if the element changed before timeoutMillis ran out, false if it did not change
     */
    public boolean didElementChangeDuringInterval(WebElement element, int timeoutMillis) {
        return comparison.didElementChangeDuringInterval(element, timeoutMillis);
    }

    /**
     * Do an image comparison of the element before and after the given interval. Returns false if the given
     * similarity threshold has been exceeded.
     * When comparing elements visually the browser zoom level and windows/mac screen scaling must be at 100%
     *
     * @param element                       WebElement to compare
     * @param similarityPercentageThreshold threshold in percentage
     * @param timeoutMillis                 time interval to wait for element comparison
     * @return true if the element changed before timeoutMillis ran out, false if it did not change
     */
    public boolean didElementChangeDuringInterval(WebElement element, float similarityPercentageThreshold, int timeoutMillis) {
        return comparison.didElementChangeDuringInterval(element, similarityPercentageThreshold, timeoutMillis);
    }

    /**
     * Wait for the value to be present in the given elements attribute within the time frame.
     *
     * @param element       WebElement containing attribute
     * @param attribute     css or html attribute
     * @param value         expected value of attribute
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForAttributeValueToBePresent(WebElement element, String attribute, String value, int timeoutMillis) {
        presence.waitForAttributeValueToBePresent(element, attribute, value, timeoutMillis);
    }

    /**
     * Check if the value is present in the given elements attribute within the time frame.
     *
     * @param element       WebElement containing attribute
     * @param attribute     css or html attribute
     * @param value         expected value of attribute
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the value was present in the attribute within time, otherwise false
     */
    public boolean isAttributeValuePresentWithinTime(WebElement element, String attribute, String value, int timeoutMillis) {
        return presence.isAttributeValuePresentWithinTime(element, attribute, value, timeoutMillis);
    }

    /**
     * Wait for the elements attribute value to contain the given value within the time frame.
     *
     * @param element       WebElement containing attribute
     * @param attribute     css or html attribute
     * @param value         value the attributes value should contain
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForAttributeValueToContain(WebElement element, String attribute, String value, int timeoutMillis) {
        presence.waitForAttributeValueToContain(element, attribute, value, timeoutMillis);
    }

    /**
     * Check if the elements attribute value contains the given value within the time frame.
     *
     * @param element       WebElement containing attribute
     * @param attribute     css or html attribute
     * @param value         value the attributes value should contain
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the attributes value contained given value within time, otherwise false
     */
    public boolean doesAttributeValueContainWithinTime(WebElement element, String attribute, String value, int timeoutMillis) {
        return presence.doesAttributeValueContainWithinTime(element, attribute, value, timeoutMillis);
    }

    /**
     * Wait for elements to be displayed within the time frame. Displayed means the element is visible on screen,
     * it does not have to be enabled.
     *
     * @param elements      List of elements to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementsToBeDisplayed(List<WebElement> elements, int timeoutMillis) {
        presence.waitForElementsToBeDisplayed(elements, timeoutMillis);
    }

    /**
     * Check if the elements is displayed within the time frame. Displayed means
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param elements      List of elements to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the elements was displayed before the timeoutMillis ran out, false if they weren't.
     */
    public boolean isElementsDisplayedWithinTime(List<WebElement> elements, int timeoutMillis) {
        return presence.isElementsDisplayedWithinTime(elements, timeoutMillis);
    }

    /**
     * Wait for elements to be hidden within the time frame. Hidden means the element is
     * not visible on screen but present in the DOM.
     *
     * @param elements      List of elements to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForElementsToBeHidden(List<WebElement> elements, int timeoutMillis) {
        presence.waitForElementsToBeHidden(elements, timeoutMillis);
    }

    /**
     * Check if the elements is hidden within the time frame. Hidden means the element is
     * not visible on screen but present in the DOM.
     *
     * @param elements      List of elements to inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the elements was hidden before the timeoutMillis ran out, false if they weren't.
     */
    public boolean isElementsHiddenWithinTime(List<WebElement> elements, int timeoutMillis) {
        return presence.isElementsHiddenWithinTime(elements, timeoutMillis);
    }

    /**
     * Wait the located child of the parent element to be displayed within the time frame. If more than one nested
     * element is found using the given locator the element found first is chosen. Displayed means that
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param element       Parent element
     * @param childLocator  By to search for children and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForNestedElementToBeDisplayed(WebElement element, By childLocator, int timeoutMillis) {
        presence.waitForNestedElementsToBeDisplayed(element, childLocator, timeoutMillis);
    }

    /**
     * Wait for all located children of parent element to be displayed within the time frame. Displayed means that
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param element       Parent element
     * @param childLocator  By to search for children and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     */
    public void waitForNestedElementsToBeDisplayed(WebElement element, By childLocator, int timeoutMillis) {
        presence.waitForNestedElementsToBeDisplayed(element, childLocator, timeoutMillis);
    }

    /**
     * Check if the located child of the parent element is displayed within the time frame. If more than one nested
     * element is found using the given locator the element found first is chosen. Displayed means the element is visible on
     * screen, it does not have to be enabled.
     *
     * @param element       Parent element
     * @param childLocator  By to search for children and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was displayed before the timeoutMillis ran out, false if they weren't.
     */
    public boolean isNestedElementDisplayedWithinTime(WebElement element, By childLocator, int timeoutMillis) {
        return presence.isNestedElementDisplayedWithinTime(element, childLocator, timeoutMillis);
    }

    /**
     * Check if all located children of the parent element is displayed within the time frame. Displayed means
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param element       Parent element
     * @param childLocator  By to search for children and inspect
     * @param timeoutMillis time to wait in milliseconds, throws a TimeoutException if time runs out.  Set to -1 to use current implicit timeoutMillis value.
     * @return true if the elements was displayed before the timeoutMillis ran out, false if they weren't.
     */
    public boolean isNestedElementsDisplayedWithinTime(WebElement element, By childLocator, int timeoutMillis) {
        return presence.isNestedElementsDisplayedWithinTime(element, childLocator, timeoutMillis);
    }

    /**
     * Inspect a list of elements and return the elements that is currently displayed. Displayed means
     * the element is visible on screen, it does not have to be enabled.
     *
     * @param elements List of elements to inspect
     * @return List of displayed elements from the given list.
     */
    public List<WebElement> getDisplayedElements(List<WebElement> elements) {
        return presence.getDisplayedElements(elements);
    }

    /**
     * Inspect a list of elements and return the elements that is currently hidden. Hidden means the element is
     * not visible on screen but present in the DOM.
     *
     * @param elements List of elements to inspect
     * @return List of hidden elements from the given list.
     */
    public List<WebElement> getHiddenElements(List<WebElement> elements) {
        return presence.getHiddenElements(elements);
    }


    /**
     * Opens an elements linked resource in the current tab
     *
     * @param webElement    element which linked resource should be opened
     * @param timeoutMillis time to wait in milliseconds. Set to -1 to use current implicit timeoutMillis value.
     * @return true if the element was clickable within time span, false if it wasn´t.
     */
    public boolean openElementLinkInSameTab(WebElement webElement, int timeoutMillis) {
        return presence.openElementLinkInSameTab(webElement, timeoutMillis);
    }

    /**
     * Opens an elements linked resource in the current tab
     *
     * @param webElement element which linked resource should be opened
     * @return true if the element was clickable, false if it wasn´t.
     */
    public boolean openElementLinkInSameTab(WebElement webElement) {
        return presence.openElementLinkInSameTab(webElement, DEFAULT_TIMEOUT_MILLIS);
    }

    /**
     * Returns elements location (x,y) relative to the viewport
     *
     * @param webElement element to locate
     * @return Point containing x and y position of the element
     */
    public Point getElementLocationInViewport(WebElement webElement) {
        return presence.getElementLocationInViewport(webElement);
    }

    /**
     * Check if the element is visible within the viewport
     *
     * @param webElement element to check
     * @return true if element is visible within the viewport, false if not.
     */
    public boolean isElementVisibleWithinViewport(WebElement webElement) {
        return presence.isElementVisibleWithinViewport(webElement);
    }

    /**
     * Change attribute value for specific element
     *
     * @param webElement element that value is changed for
     * @param attribute  attribute to change value for
     * @param value      new value for attribute
     */
    public void changeElementAttributeValue(WebElement webElement, String attribute, String value) {
        presence.changeElementAttributeValue(webElement, attribute, value);
    }


}
