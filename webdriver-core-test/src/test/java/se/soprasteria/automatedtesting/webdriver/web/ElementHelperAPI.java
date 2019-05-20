package se.soprasteria.automatedtesting.webdriver.web;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.io.IOException;
import static se.soprasteria.automatedtesting.webdriver.web.datastructure.Page.*;
import static se.soprasteria.automatedtesting.webdriver.web.datastructure.ElementState.*;
import static se.soprasteria.automatedtesting.webdriver.web.datastructure.ScreenshotElement.*;

public class ElementHelperAPI extends WebBaseTestCase {

    //TIMEOUTS in browser currently set to 1000s
    private final int TIME_WAIT_LONG = 20000;
    private final int TIME_LONG_ENOUGH = 2000;
    private final int TIME_TOO_SHORT = 200;
    private final int ELEMENT_DELAY_SHORT = 250;
    private final int ELEMENT_DELAY_LONG = 750;
    //SCREENSHOT VARIABLES corresponding to values in the browser.
    private final int COLORED_ELEMENT1_MAX_THRESHOLD = 53;
    private final int COLORED_ELEMENT1_MIN_THRESHOLD = 51;
    private final int COLORED_ELEMENT2_MAX_THRESHOLD = 86;
    private final int COLORED_ELEMENT2_MIN_THRESHOLD = 84;
    private final int COLORED_ELEMENT3_MAX_THRESHOLD = 29;
    private final int COLORED_ELEMENT3_MIN_THRESHOLD = 27;
    private final int COLORED_ELEMENT4_MAX_THRESHOLD = 68;
    private final int COLORED_ELEMENT4_MIN_THRESHOLD = 66;
    private final int COLORED_ELEMENT5_MAX_THRESHOLD = 55;
    private final int COLORED_ELEMENT5_MIN_THRESHOLD = 53;
    private final int BLACK_RECT_ELEMENT2_3_MAX_THRESHOLD = 67;
    private final int BLACK_RECT_ELEMENT2_3_MIN_THRESHOLD = 65;
    private final int BLACK_RECT_ELEMENT1_3_MAX_THRESHOLD = 34;
    private final int BLACK_RECT_ELEMENT1_3_MIN_THRESHOLD = 32;
    private final int ROTATION_ELEMENT_MAX_THRESHOLD = 98;
    private final int ROTATION_ELEMENT_MIN_THRESHOLD = 93;
    private final int MAXIMUM_THRESHOLD = 100;
    private final String FAILING_ATTRIBUTE_VALUE = "pepparkaka";

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void clickWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(clickElementMethods.clickWithinTime(), "Element was clickable");
        clickElementMethods.clickShowElementButton();
        Assert.assertTrue(clickElementMethods.clickWithinTime(),"Element was not clickable");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void clickWithinTimeWithMessage(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(clickElementMethods.clickWithinTimeWithMessage(), "Element was clickable");
        clickElementMethods.clickShowElementButton();
        Assert.assertTrue(clickElementMethods.clickWithinTimeWithMessage(), "Element was not clickable");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementClickableWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerShowButton();
        Assert.assertTrue(isElementMethods.isElementClickableWithinTime(TIME_LONG_ENOUGH), "Element was not clickable within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerShowButton();
        Assert.assertFalse(isElementMethods.isElementClickableWithinTime(TIME_TOO_SHORT), "Element was clickable within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementDisplayedWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerShowButton();
        Assert.assertTrue(isElementMethods.isElementDisplayedWithinTime(TIME_LONG_ENOUGH), "Element was not displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isElementDisplayedWithinTime(TIME_TOO_SHORT), "Element was displayed within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementHiddenOrGoneWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertTrue(isElementMethods.isElementHiddenOrGoneWithinTime(TIME_LONG_ENOUGH,true), "Element is removed from DOM within time");
        clickElementMethods.clickTimerHideButton();
        Assert.assertTrue(isElementMethods.isElementHiddenOrGoneWithinTime(TIME_LONG_ENOUGH, false), "Element is hidden within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertFalse(isElementMethods.isElementHiddenOrGoneWithinTime(TIME_TOO_SHORT,true), "Element is not be removed from DOM within time");
        clickElementMethods.clickTimerHideButton();
        Assert.assertFalse(isElementMethods.isElementHiddenOrGoneWithinTime(TIME_TOO_SHORT, false), "Element is not hidden within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementGoneOrIsHiddenWithinTimeWithElement(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertTrue(isElementMethods.isElementHiddenOrGoneWithinTimeWithElement(TIME_LONG_ENOUGH,true), "Element is removed from DOM within time");
        clickElementMethods.clickTimerHideButton();
        Assert.assertTrue(isElementMethods.isElementHiddenOrGoneWithinTimeWithElement(TIME_LONG_ENOUGH, false), "Element is hidden within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertFalse(isElementMethods.isElementHiddenOrGoneWithinTimeWithElement(TIME_TOO_SHORT,true), "Element is not removed from DOM within time");
        clickElementMethods.clickTimerHideButton();
        Assert.assertFalse(isElementMethods.isElementHiddenOrGoneWithinTimeWithElement(TIME_TOO_SHORT, false), "Element is not hidden within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementGoneWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertTrue(isElementMethods.isElementGoneWithinTime(TIME_LONG_ENOUGH), "Element is not removed from DOM");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerRemoveButton();
        Assert.assertFalse(isElementMethods.isElementGoneWithinTime(TIME_TOO_SHORT), "Element is removed from DOM");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementHiddenWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerHideButton();
        Assert.assertTrue(isElementMethods.isElementHiddenWithinTime(TIME_LONG_ENOUGH), "Element is not hidden within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerHideButton();
        Assert.assertFalse(isElementMethods.isElementHiddenWithinTime(TIME_TOO_SHORT), "Element is hidden within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementPresentAndDisplayed(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        //Element is displayed
        Assert.assertTrue(isElementMethods.isElementPresentAndDisplayed(true, true), "Test failed to recognize the element as present and displayed");
        //Element is present but not displayed
        Assert.assertFalse(isElementMethods.isElementPresentAndDisplayed(true, false), "Element is present in the DOM but NOT displayed");
        //Element is not present
        Assert.assertFalse(isElementMethods.isElementPresentAndDisplayed(false, false), "Element is not present in the DOM");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementPresentAndDisplayedWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickCreateAndDisplayNewElementButton();
        Assert.assertTrue(isElementMethods.isElementPresentAndDisplayedWithinTime(ELEMENT_WILL_BE_DISPLAYED, TIME_LONG_ENOUGH), "Test failed to recognize the element as present and displayed within time");
        clickElementMethods.clickCreateAndHideNewElementButton();
        Assert.assertFalse(isElementMethods.isElementPresentAndDisplayedWithinTime(ELEMENT_WILL_BE_HIDDEN, TIME_LONG_ENOUGH), "Element is present in the DOM but NOT displayed");
        mainPage.reloadPage(driver);
        clickElementMethods.clickCreateAndDisplayNewElementButton();
        Assert.assertFalse(isElementMethods.isElementPresentAndDisplayedWithinTime(ELEMENT_WILL_BE_DISPLAYED, TIME_TOO_SHORT), "Test failed, should not have recognized the element as present and displayed within the timeframe.");
        clickElementMethods.clickCreateAndHideNewElementButton();
        Assert.assertFalse(isElementMethods.isElementPresentAndDisplayedWithinTime(ELEMENT_WILL_BE_HIDDEN, TIME_TOO_SHORT), "Element is present in the DOM but NOT displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsElementPresentWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertFalse(isElementMethods.isElementPresentWithinTime(TIME_TOO_SHORT), "Element was present within time");
        Assert.assertTrue(isElementMethods.isElementPresentWithinTime(TIME_LONG_ENOUGH), "Element was not present within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void IsTextPresentInElementWithinTime(AutomationDriver driver) {
        //Checks to see if the text is present in the element within the specific time.
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowTextButton();
        Assert.assertTrue(isElementMethods.isTextPresentInElementWithinTime(), "Text was not present");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isTextPresentInElementWithinTime(), "Text was present");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void SendKeysWithControlledSpeed(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(elementMethods.sendKeysWithControlledSpeed());
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void WaitForElementToBeClickable(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerShowButton(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_CLICKABLE,TIME_WAIT_LONG,ELEMENT_DELAY_SHORT), "Method did not break timeout as soon as element was clickable");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_CLICKABLE,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Element was detected and clickable");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerShowButton(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_CLICKABLE,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Element was detected and clickable");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void WaitForElementToBeDisplayed(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerShowButton(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_DISPLAYED,TIME_WAIT_LONG,ELEMENT_DELAY_SHORT), "Method did not break timeout as soon as element was displayed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_DISPLAYED,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Element was detected and displayed");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerShowButton(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_DISPLAYED,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Element was detected and displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void WaitForElementToBeGone(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerRemoveButton(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_REMOVED,TIME_WAIT_LONG,ELEMENT_DELAY_LONG), "Method did not break timeout as soon as element was removed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_REMOVED,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Element was detected and removed");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerRemoveButton(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_REMOVED,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Element was detected and removed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void WaitForElementToBeHidden(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickTimerHideButton(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_HIDDEN,TIME_WAIT_LONG,ELEMENT_DELAY_LONG));
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_HIDDEN,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Element was hidden");
        mainPage.reloadPage(driver);
        clickElementMethods.clickTimerHideButton(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(ELEMENT_WILL_BE_HIDDEN,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Element was hidden");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void WaitForTextToBePresentInElement(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowTextButton(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(TEXT_WILL_BE_PRESENT,TIME_WAIT_LONG,ELEMENT_DELAY_SHORT), "Method did not break timeout as soon as text was present in element;");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(TEXT_WILL_BE_PRESENT,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Text was present in element");
        mainPage.reloadPage(driver);
        clickElementMethods.clickShowTextButton(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(TEXT_WILL_BE_PRESENT,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Text was present in element within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void openElementLinkInSameTab(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(elementMethods.openLinkInSameTab(),"Page failed to load in the same tab");
        Assert.assertTrue(isElementMethods.isGoogleLogoPresent(), "Google logo was not present");
        Assert.assertTrue(isElementMethods.isOneTabOpened(),"Page opened in another tab");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void openElementLinkInSameTabWithinTime(AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(clickElementMethods.clickShowGoToAnotherPageButton(), "Element was not clickable");
        Assert.assertTrue(elementMethods.openLinkInSameTabWithinTime(TIME_LONG_ENOUGH), "Element was not clickable within time");
        Assert.assertTrue(isElementMethods.isGoogleLogoPresent(), "Google logo was not present");
        Assert.assertTrue(isElementMethods.isOneTabOpened(), "Page opened in another tab");
        mainPage.reloadPage(driver);
        Assert.assertTrue(clickElementMethods.clickShowGoToAnotherPageButton(), "Element was not clickable");
        Assert.assertFalse(elementMethods.openLinkInSameTabWithinTime(TIME_TOO_SHORT), "Element was clickable within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isElementVisibleWithinViewport(AutomationDriver driver) {
        initialize(SCROLL_PAGE);
        scrollPage.scrollToElement();
        Assert.assertTrue(scrollPage.isElementVisibleWithinViewport(), "Element was not visible in the viewport.");
        scrollPage.scrollToTop();
        Assert.assertFalse(scrollPage.isElementVisibleWithinViewport(),"Element was visible in the viewport.");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void getElementLocationInViewport(AutomationDriver driver) {
        initialize(ABSOLUTE_ELEMENT_PAGE);
        absoluteElementPage.getElementsCSSLocation();
        absoluteElementPage.getElementLocationInViewPort();
        Assert.assertTrue(absoluteElementPage.isPointsEqual(),"Points from elements CSS and point from API-method did not match");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser"})
    public void didElementChangeDuringInterval_definedSimilarity (AutomationDriver driver) {
        initialize(ELEMENT_SCREENSHOT_PAGE);
        elementScreenshotPage.clickStartRotationButton();
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(ROTATION_ELEMENT, ROTATION_ELEMENT_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + ROTATION_ELEMENT_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.reloadPage(driver);
        elementScreenshotPage.clickStartRotationButton();
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(ROTATION_ELEMENT, ROTATION_ELEMENT_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ ROTATION_ELEMENT_MIN_THRESHOLD + "%");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser"})
    public void didElementChangeDuringInterval (AutomationDriver driver) throws IOException {
        initialize(ELEMENT_SCREENSHOT_PAGE);
        elementScreenshotPage.clickStartRotationButton();
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(), "Element did change during interval");
        elementScreenshotPage.reloadPage(driver);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(), "Element did not change during interval");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void doesAttributeValueContainWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        String failingValue = "pepparkaka";
        clickElementMethods.clickChangeValueButton();
        Assert.assertTrue(elementMethods.doesAttributeValueContainWithinTime("text"), "Attribute value did change within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(elementMethods.doesAttributeValueContainWithinTime(failingValue), "Atrribute value did change within time but not to " +failingValue);
        mainPage.reloadPage(driver);
        Assert.assertFalse(elementMethods.doesAttributeValueContainWithinTime("text"), "Attribute value did not change within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser"})
    public void elementScreenshot_BlackRect (AutomationDriver driver) throws IOException {
        initialize(ELEMENT_SCREENSHOT_PAGE);
        elementScreenshotPage.clickRectButton2_3();
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(BLACK_RECT_ELEMENT, BLACK_RECT_ELEMENT2_3_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + BLACK_RECT_ELEMENT2_3_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickRectButton3_3();
        elementScreenshotPage.clickRectButton2_3();
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(BLACK_RECT_ELEMENT, BLACK_RECT_ELEMENT2_3_MIN_THRESHOLD), "The element changed during the interval and had a similarity percentage below the threshold of "+ BLACK_RECT_ELEMENT2_3_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickRectButton3_3();
        elementScreenshotPage.clickRectButton1_3();
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(BLACK_RECT_ELEMENT, BLACK_RECT_ELEMENT1_3_MAX_THRESHOLD), "The element had a similarity percentage above the threshold of " + BLACK_RECT_ELEMENT1_3_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickRectButton3_3();
        elementScreenshotPage.clickRectButton1_3();
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(BLACK_RECT_ELEMENT, BLACK_RECT_ELEMENT1_3_MIN_THRESHOLD), "The element changed during the interval and had a similarity percentage below the threshold of "+ BLACK_RECT_ELEMENT1_3_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickRectButton3_3();
        elementScreenshotPage.clickRectButton0_3();
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(BLACK_RECT_ELEMENT,MAXIMUM_THRESHOLD),"The element changed less then "+MAXIMUM_THRESHOLD+"%");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser"})
    public void elementScreenshot_Colors (AutomationDriver driver) throws IOException {
        initialize(ELEMENT_SCREENSHOT_PAGE);
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_1);
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_1, COLORED_ELEMENT1_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + COLORED_ELEMENT1_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_1);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_1, COLORED_ELEMENT1_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ COLORED_ELEMENT1_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_2);
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_2, COLORED_ELEMENT2_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + COLORED_ELEMENT2_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_2);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_2, COLORED_ELEMENT2_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ COLORED_ELEMENT2_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_3);
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_3, COLORED_ELEMENT3_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + COLORED_ELEMENT3_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_3);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_3, COLORED_ELEMENT3_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ COLORED_ELEMENT3_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_4);
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_4, COLORED_ELEMENT4_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + COLORED_ELEMENT4_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_4);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_4, COLORED_ELEMENT4_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ COLORED_ELEMENT4_MIN_THRESHOLD + "%");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_5);
        Assert.assertTrue(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_5, COLORED_ELEMENT5_MAX_THRESHOLD),"The element had a similarity percentage above the threshold of " + COLORED_ELEMENT5_MAX_THRESHOLD + "% after the interval");
        elementScreenshotPage.clickResetColors();
        elementScreenshotPage.clickColorButton(COLORED_ELEMENT_5);
        Assert.assertFalse(elementScreenshotPage.didElementChangeDuringInterval(COLORED_ELEMENT_5, COLORED_ELEMENT5_MIN_THRESHOLD),"The element changed during the interval and had a similarity percentage below the threshold of "+ COLORED_ELEMENT5_MIN_THRESHOLD + "%");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void getDisplayedElements (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(isElementMethods.isDisplayedElementsListCorrect(), "List did not contain correct amount of displayed elements");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void getHiddenElements (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        Assert.assertTrue(isElementMethods.isHiddenElementsListCorrect(), "List did not contain correct amount of hidden elements");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isAttributeValuePresentWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickChangeValueButton();
        Assert.assertTrue(isElementMethods.isAttributeValuePresentWithinTime("text"), "Attribute value did change within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(isElementMethods.isAttributeValuePresentWithinTime(FAILING_ATTRIBUTE_VALUE), "Atrribute value did change within time but not to " +FAILING_ATTRIBUTE_VALUE);
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isAttributeValuePresentWithinTime("text"), "Attribute value did not change within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isElementClickableAndRefreshedWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertTrue(isElementMethods.isElementClickableAndRefreshedWithinTime(TIME_LONG_ENOUGH), "Element is enabled within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertFalse(isElementMethods.isElementClickableAndRefreshedWithinTime(ELEMENT_DELAY_LONG), "Element is not enabled within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isElementClickableAndRefreshedWithinTime(TIME_LONG_ENOUGH), "Element is never displayed in the viewport");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isElementDisplayedAndRefreshedWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertTrue(isElementMethods.isElementDisplayedAndRefreshedWithinTime(TIME_LONG_ENOUGH), "Could not recognize the element as displayed and refreshed");
        mainPage.reloadPage(driver);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertFalse(isElementMethods.isElementDisplayedAndRefreshedWithinTime(TIME_TOO_SHORT),"Element should not be displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isElementDisplayedAndRefreshedWithinTime(TIME_LONG_ENOUGH), "Element is never displayed in the viewport");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isNestedElementDisplayedWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowNestedElements();
        Assert.assertTrue(isElementMethods.isNestedElementDisplayedWithinTime(TIME_LONG_ENOUGH),"Nested element is displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isNestedElementDisplayedWithinTime(TIME_TOO_SHORT), "Nested element is not displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isNestedElementDisplayedWithinTime(TIME_LONG_ENOUGH), "Nested element is never displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void isNestedElementsDisplayedWithinTime (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowNestedElements();
        Assert.assertTrue(isElementMethods.isNestedElementsDisplayedWithinTime(TIME_LONG_ENOUGH),"Nested element is displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isNestedElementsDisplayedWithinTime(TIME_TOO_SHORT), "Nested element is not displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(isElementMethods.isNestedElementsDisplayedWithinTime(TIME_LONG_ENOUGH), "Nested element is never displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForAttributeValueToBePresent (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickChangeValueButton();
        Assert.assertTrue(waitForElementMethods.waitForAttributeValueToBePresent("text", TIME_LONG_ENOUGH),"Attribute value is present in element within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToBePresent("text",TIME_TOO_SHORT), "Attribute value is not present in element within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToBePresent("text", TIME_LONG_ENOUGH), "Attribute value is not present in element within time");
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToBePresent(FAILING_ATTRIBUTE_VALUE, TIME_LONG_ENOUGH), "Atrribute value did change within time but not to " +FAILING_ATTRIBUTE_VALUE);
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForAttributeValueToContain (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickChangeValueButton();
        Assert.assertTrue(waitForElementMethods.waitForAttributeValueToContain("text", TIME_LONG_ENOUGH),"Attribute value is present in element within time");
        mainPage.reloadPage(driver);
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToContain("text",TIME_TOO_SHORT), "Attribute value is not present in element within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToContain("text", TIME_LONG_ENOUGH), "Attribute value is not present in element within time");
        clickElementMethods.clickChangeValueButton();
        Assert.assertFalse(waitForElementMethods.waitForAttributeValueToContain(FAILING_ATTRIBUTE_VALUE, TIME_LONG_ENOUGH), "Atrribute value did change within time but not to " +FAILING_ATTRIBUTE_VALUE);
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForElementToBeDisplayedAndRefreshed (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertTrue(waitForElementMethods.waitForElementToBeDisplayedAndRefreshed(TIME_WAIT_LONG, ELEMENT_DELAY_LONG),"Could not recognize the element as displayed and refreshed");
        mainPage.reloadPage(driver);
        clickElementMethods.clickDisplayAndEnableButton();
        Assert.assertFalse(waitForElementMethods.waitForElementToBeDisplayedAndRefreshed(TIME_TOO_SHORT, ELEMENT_DELAY_LONG),"Element should not be displayed within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBeDisplayedAndRefreshed(TIME_LONG_ENOUGH, ELEMENT_DELAY_SHORT),"Element is never displayed in the viewport");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForElementToBePresentAndDisplayed_By (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickCreateAndDisplayNewElementButton();
        Assert.assertTrue(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, true,TIME_WAIT_LONG,ELEMENT_DELAY_LONG),"Method didn't locate element as displayed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, true, TIME_LONG_ENOUGH, ELEMENT_DELAY_LONG), "Element was never displayed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, true, TIME_TOO_SHORT, ELEMENT_DELAY_LONG), "Method located element as displayed though it wasn't within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_HIDDEN, true, TIME_LONG_ENOUGH, ELEMENT_DELAY_SHORT),"Element was present but never displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForElementToBePresentAndDisplayed_WebElement (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickCreateAndDisplayNewElementButton();
        Assert.assertTrue(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, false,TIME_WAIT_LONG,ELEMENT_DELAY_LONG),"Method didn't locate element as displayed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, false, TIME_LONG_ENOUGH, ELEMENT_DELAY_LONG), "Element was never displayed");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_DISPLAYED, false, TIME_TOO_SHORT, ELEMENT_DELAY_LONG), "Method located element as displayed though it wasn't within time");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToBePresentAndDisplayed(ELEMENT_WILL_BE_HIDDEN, false, TIME_LONG_ENOUGH, ELEMENT_DELAY_SHORT),"Element was present but never displayed");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForNestedElementsToBeDisplayed (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowNestedElements(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForNestedElementsToBeDisplayed(TIME_WAIT_LONG,ELEMENT_DELAY_SHORT), "Method did not break timeout as soon as text was present in element;");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForNestedElementsToBeDisplayed(TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Text was present in element");
        mainPage.reloadPage(driver);
        clickElementMethods.clickShowNestedElements(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForNestedElementsToBeDisplayed(TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Text was present in element within time");
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"browser","mobile"})
    public void waitForNestedElementToBeDisplayed (AutomationDriver driver) {
        initialize(MAIN_PAGE);
        clickElementMethods.clickShowNestedElements(ELEMENT_DELAY_SHORT);
        Assert.assertTrue(waitForElementMethods.waitForElementToChangeState(NESTED_ELEMENT_WILL_BE_DISPLAYED,TIME_WAIT_LONG,ELEMENT_DELAY_SHORT), "Method did not break timeout as soon as text was present in element;");
        mainPage.reloadPage(driver);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(NESTED_ELEMENT_WILL_BE_DISPLAYED,TIME_LONG_ENOUGH,ELEMENT_DELAY_SHORT), "Text was present in element");
        mainPage.reloadPage(driver);
        clickElementMethods.clickShowNestedElements(ELEMENT_DELAY_LONG);
        Assert.assertFalse(waitForElementMethods.waitForElementToChangeState(NESTED_ELEMENT_WILL_BE_DISPLAYED,TIME_TOO_SHORT,ELEMENT_DELAY_LONG), "Text was present in element within time");
    }

}
