package se.soprasteria.automatedtesting.webdriver.helpers.element;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Checks for element presence in the class specified in constructor
 */

@SuppressWarnings("unused")
public class Presence extends BaseClass {
    private final AutomationDriver driver;
    private final int POLLING_FREQUENCY = 250;

    public Presence(AutomationDriver driver) {
        this.driver = driver;
    }

    public boolean isElementPresentAndDisplayed(final WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.info("Element is not present within the DOM");
            return false;
        }
    }

    public boolean isElementPresentAndDisplayedWithinTime(final WebElement webElement, int timeout) {
        try {
            waitForElementToBePresentAndDisplayed(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBePresentAndDisplayed(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        final int originalTimeout = timeout;
        while (true) {
            if (timeout < 0) {
                logger.trace("Element was not present and displayed within " + originalTimeout + " milliseconds");
                driver.enableImplicitTimeout();
                throw new TimeoutException("Expected condition failed: waiting for element to become present and visible (tried for " + originalTimeout + " second(s) with " + POLLING_FREQUENCY + " milliseconds interval");
            }
            try {
                if (webElement.isDisplayed()) break;
            } catch (WebDriverException | NullPointerException expected) {
            }
            sleep(POLLING_FREQUENCY);
            timeout -= POLLING_FREQUENCY;
        }
        logger.trace("Element was present and displayed within " + originalTimeout + " milliseconds");
        driver.enableImplicitTimeout();
    }

    public boolean isElementPresentWithinTime(By by, int timeout) {
        try {
            waitForElementToBePresent(by, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBePresent(By by, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.presenceOfElementLocated(by));
            logger.trace("Element using the locator " + by.toString() +  " was present within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Element using the locator " + by.toString() +  " was not present within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isTextPresentWithinTime(WebElement webElement, String text, int timeout) {
        try {
            waitForTextToBePresentInElement(webElement, text, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForTextToBePresentInElement(WebElement webElement, String text, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.textToBePresentInElement(webElement, text));
            logger.trace("Text('" + text + "') was present in element within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Text('" + text + "') was not present in element within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementDisplayedWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeDisplayed(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeDisplayed(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.visibilityOf(webElement));
            logger.trace("Element was displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Element was not displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementDisplayedWithinTime(By by, int timeout) {
        try {
            waitForElementToBePresentAndDisplayed(by, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBePresentAndDisplayed(By by, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
            logger.trace("Element using the locator " + by.toString() +  " was displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Element using the locator " + by.toString() +  " was not displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementGoneWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeGone(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeGone(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        final int originalTimeout = timeout;
        while (true) {
            if (timeout < 0) {
                logger.trace("Element was not gone within " + originalTimeout + " milliseconds");
                driver.enableImplicitTimeout();
                throw new TimeoutException("Expected condition failed: waiting for element to be gone (tried for " + originalTimeout + " second(s) with " + POLLING_FREQUENCY + " milliseconds interval");
            }
            try {
                webElement.isEnabled();
            } catch (WebDriverException | NullPointerException expected) {
                break;
            }
            sleep(POLLING_FREQUENCY);
            timeout -= POLLING_FREQUENCY;
        }
        logger.trace("Element was gone within " + originalTimeout + " milliseconds");
        driver.enableImplicitTimeout();
    }

    public boolean isElementHiddenWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeHidden(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeHidden(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.invisibilityOf(webElement));
            logger.trace("Element was hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (TimeoutException e) {
            logger.trace("Element was not hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        } catch (WebDriverException e) {
            logger.trace("Element could not be found");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementHiddenOrGoneWithinTime(By by, int timeout) {
        try {
            waitForElementToBeHiddenOrGone(by, timeout);
            return true;
        } catch (WebDriverException e) {
            logger.trace("Element still present and visible (note: this could be expected), used the locator " + by.toString());
            return false;
        }
    }

    public void waitForElementToBeHiddenOrGone(By by, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(by));
            logger.trace("Element using the locator " + by.toString() +  " was either gone or hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (TimeoutException e) {
            logger.trace("Element using the locator " + by.toString() +  " was not gone or hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        } catch (WebDriverException expected) {
            logger.trace("Element using the locator " + by.toString() +  " was gone within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        }
    }

    public boolean isElementHiddenOrGoneWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeHiddenOrGone(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeHiddenOrGone(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        final int originalTimeout = timeout;
        while (true) {
            if (timeout < 0) {
                logger.trace("Element was not gone or hidden within " + originalTimeout + " milliseconds");
                driver.enableImplicitTimeout();
                throw new TimeoutException("Expected condition failed: waiting for element to become present and visible (tried for " + originalTimeout + " second(s) with " + POLLING_FREQUENCY + " milliseconds interval");
            }
            try {
                if (!webElement.isDisplayed()) {
                    logger.trace("Element was hidden within " + originalTimeout + " milliseconds");
                    break;
                }
            } catch (WebDriverException | NullPointerException expected) {
                logger.trace("Element was gone within " + originalTimeout + " milliseconds");
                break;
            }
            sleep(POLLING_FREQUENCY);
            timeout -= POLLING_FREQUENCY;
        }
        driver.enableImplicitTimeout();
    }

    public boolean isElementClickableWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeClickable(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeClickable(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.elementToBeClickable(webElement));
            logger.trace("Element was clickable within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException|NullPointerException e) {
            logger.trace("Element was not clickable within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    private WebDriverWait getWebDriverWait(int timeoutMillis) {
        if (timeoutMillis == -1) {
            timeoutMillis = driver.getImplicitTimeout();
        }
        WebDriverWait webDriverWait = new WebDriverWait(driver, (Long.valueOf(timeoutMillis)/1000));
        webDriverWait.pollingEvery(Duration.ofMillis(POLLING_FREQUENCY));
        return webDriverWait;
    }

    public boolean isElementDisplayedAndRefreshedWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeDisplayedAndRefreshed(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeDisplayedAndRefreshed(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(webElement)));
            logger.trace("Element was displayed and refreshed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Element was not displayed or refreshed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementClickableAndRefreshedWithinTime(WebElement webElement, int timeout) {
        try {
            waitForElementToBeClickableAndRefreshed(webElement, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementToBeClickableAndRefreshed(WebElement webElement, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
            logger.trace("Element was clickable and refreshed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Element was not clickable and refreshed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isAttributeValuePresentWithinTime(WebElement webElement, String attribute, String value, int timeout) {
        try {
            waitForAttributeValueToBePresent(webElement, attribute, value, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForAttributeValueToBePresent(WebElement webElement, String attribute, String value, int timeout){
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.attributeToBe(webElement, attribute, value));
            logger.trace("Value('" + value + "') was present in Attribute('" + attribute + "') within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("Value('" + value + "') was not present in Attribute('" + attribute + "') within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean doesAttributeValueContainWithinTime(WebElement webElement, String attribute, String value, int timeout) {
        try {
            waitForAttributeValueToContain(webElement, attribute, value, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForAttributeValueToContain(WebElement webElement, String attribute, String value, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.attributeContains(webElement, attribute, value));
            logger.trace("The Value of Attribute('" + attribute + "') did contain Value('" + value + "') within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("The Value of Attribute('" + attribute + "') did not contain Value('" + value + "') within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementsDisplayedWithinTime(List<WebElement> elements, int timeout) {
        try {
            waitForElementsToBeDisplayed(elements, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementsToBeDisplayed(List<WebElement> elements, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.visibilityOfAllElements(elements));
            logger.trace("All elements were displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("All elements were not displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isElementsHiddenWithinTime(List<WebElement> elements, int timeout) {
        try {
            waitForElementsToBeHidden(elements, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForElementsToBeHidden(List<WebElement> elements, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.invisibilityOfAllElements(elements));
            logger.trace("All elements were hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e) {
            logger.trace("All elements were not hidden within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public boolean isNestedElementDisplayedWithinTime(WebElement parentElement, By childLocator, int timeout) {
        try {
            waitForNestedElementToBeDisplayed(parentElement, childLocator, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForNestedElementToBeDisplayed(WebElement parentElement, By childLocator, int timeout) {
        List<WebElement> nestedElements = parentElement.findElements(childLocator);
        if (nestedElements.size() == 0)
            throw new NullPointerException("No nested elements could be found using the locator " +
                    childLocator.toString());
        if (nestedElements.size() > 1) logger.trace("Located more than one nested element using the locator " +
                childLocator + ", checking visibility of the first element found");
        WebElement nestedElement = nestedElements.get(0);

        driver.disableImplicitTimeout();
        final int originalTimeout = timeout;
        while (true) {
            if (timeout < 0) {
                logger.trace("Nested element using the locator " + childLocator.toString() + " was not displayed within " +
                        timeout + " milliseconds");
                driver.enableImplicitTimeout();
                throw new TimeoutException("Expected condition failed: waiting for nested element to become visible (tried for " + originalTimeout + " second(s) with " + POLLING_FREQUENCY + " milliseconds interval");
            }
            if (nestedElement.isDisplayed()) break;
            sleep(POLLING_FREQUENCY);
            timeout -= POLLING_FREQUENCY;
        }
        logger.trace("Nested element was displayed within " + originalTimeout + " milliseconds");
        driver.enableImplicitTimeout();

    }

    public boolean isNestedElementsDisplayedWithinTime(WebElement parentElement, By childLocator, int timeout) {
        try {
            waitForNestedElementsToBeDisplayed(parentElement, childLocator, timeout);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public void waitForNestedElementsToBeDisplayed(WebElement parentElement, By childLocator, int timeout) {
        driver.disableImplicitTimeout();
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentElement, childLocator));
            logger.trace("All nested elements using the locator " + childLocator.toString() +  " were displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
        } catch (WebDriverException e){
            logger.trace("All nested elements using the locator " + childLocator.toString() +  " were not displayed within " + timeout + " milliseconds");
            driver.enableImplicitTimeout();
            throw e;
        }
    }

    public List<WebElement> getDisplayedElements(List<WebElement> elements) {
        driver.disableImplicitTimeout();
        List<WebElement> displayedElements = new ArrayList<>();
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed()) displayedElements.add(element);
            } catch (WebDriverException | NullPointerException expected) {
            }
        }
        logger.trace(displayedElements.size() + " of " + elements.size() + " elements were displayed");
        driver.enableImplicitTimeout();
        return displayedElements;
    }

    public List<WebElement> getHiddenElements(List<WebElement> elements) {
        driver.disableImplicitTimeout();
        List<WebElement> hiddenElements = new ArrayList<>();
        for (WebElement element : elements) {
            try {
                if (!element.isDisplayed()) hiddenElements.add(element);
            } catch (WebDriverException | NullPointerException expected) {
            }
        }
        logger.trace(hiddenElements.size() + " of " + elements.size() + " elements were hidden");
        driver.enableImplicitTimeout();
        return hiddenElements;
    }

    private String generateXpathFromWebElement(WebElement childElement, String current) {
        String childTag = childElement.getTagName();
        if (childTag.equals("html")) {
            return "/html[1]" + current;
        }
        WebElement parentElement = childElement.findElement(By.xpath(".."));
        List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
        int count = 0;
        for (int i = 0; i < childrenElements.size(); i++) {
            WebElement childrenElement = childrenElements.get(i);
            String childrenElementTag = childrenElement.getTagName();
            if (childTag.equals(childrenElementTag)) {
                count++;
            }
            if (childElement.equals(childrenElement)) {
                return generateXpathFromWebElement(parentElement, "/" + childTag + "[" + count + "]" + current);
            }
        }
        return null;
    }

    public boolean openElementLinkInSameTab(WebElement webElement, int timeOutMillis){
        driver.executeJavaScript("document.querySelector(arguments[0].setAttribute('target', '_self'))", webElement);
        if (isElementClickableWithinTime(webElement, timeOutMillis)) {
            webElement.click();
            return true;
        }
        return false;
    }

    public Point getElementLocationInViewport(WebElement webElement){
        Map map = (Map) driver.executeJavaScript(
                "function Point (x, y){this.x = x; this.y = y;}" +
                        "return new Point(" +
                        "arguments[0].getBoundingClientRect().left, " +
                        "arguments[0].getBoundingClientRect().top);"
                , webElement);
        int x = ((Number) map.get("x")).intValue();
        int y = ((Number) map.get("y")).intValue();
        return new Point(x, y);
    }

    public boolean isElementVisibleWithinViewport(WebElement webElement) {
        return (Boolean)driver.executeJavaScript(
                "elementBox = arguments[0].getBoundingClientRect(),      "+
                        "centerX = elementBox.left + elementBox.width / 2,      "+
                        "centerY = elementBox.top + elementBox.height / 2,      "+
                        "element = document.elementFromPoint(centerX, centerY); "+
                        "for (; element; element = element.parentElement){      "+
                        "  if (arguments[0] === element)                        "+
                        "    return true;}                                      "+
                        "return false;"
                , webElement);
    }

    public void changeElementAttributeValue(WebElement webElement, String attribute, String value){
        driver.executeJavaScript("document.querySelector(arguments[0].setAttribute('" + attribute + "', '" + value + "'))", webElement);
    }

    public void click(WebElement webElement) {
        webElement.click();
    }

}
