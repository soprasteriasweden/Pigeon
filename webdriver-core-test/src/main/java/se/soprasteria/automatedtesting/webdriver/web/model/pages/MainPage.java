package se.soprasteria.automatedtesting.webdriver.web.model.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.WebBasePageObject;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends WebBasePageObject {

    protected final String JS_FUNCTION_REMOVE =  "removeAfterTimeout";
    protected final String JS_FUNCTION_HIDE =  "hideAfterTimeout";
    protected final String JS_FUNCTION_SHOW =  "showAfterTimeout";
    protected final String JS_FUNCTION_SHOW_BY_CLASS =  "showAfterTimeoutByClass";
    protected final String JS_FUNCTION_INSERT_TEXT = "insertTextAfterTimeout";
    protected final String EXAMPLE_STRING = "Sopra Steria Sweden AB";
    @FindBy(css = "h1[class='page-header']")
    protected WebElement pageTitleHeader;
    @FindBy(css = "button[id='showelementaftertime']")
    protected WebElement showElementTimerButton;
    @FindBy(css = "a[id='delayedelement']")
    protected WebElement delayedElement;
    @FindBy(css = "button[id='showelement']")
    protected WebElement showClickableElementButton;
    @FindBy(css = "a[id='clickableelement']")
    protected WebElement clickableElement;
    @FindBy(css = "button[id='hideelementaftertime']")
    protected WebElement hideElementTimerButton;
    @FindBy(css = "a[id='delayedhiddenelement']")
    protected WebElement delayedHiddenElement;
    @FindBy(css = "button[id='showtext']")
    protected WebElement showTextButton;
    @FindBy(css = "p[id='text']")
    protected WebElement text;
    @FindBy(css = "button[id='removeelementaftertime']")
    protected WebElement removeElementTimerButton;
    @FindBy(css = "a[id='delayedremovedelement']")
    protected WebElement delayedRemovedElement;
    //Button that onclick hides an element
    @FindBy(css = "button[id='createandhidenewbutton']")
    protected WebElement createAndHideNewElementButton;
    @FindBy(css = "button[id='createdhiddenbutton']")
    protected WebElement createdAndHiddenElement;
    @FindBy(css = "button[id='createnewelement']")
    protected WebElement createAndDisplayNewElementButton;
    @FindBy(css = "button[id='createdelement']")
    protected WebElement createdAndDisplayedElement;
    @FindBy(css = "button[id='hiddenelement']")
    protected WebElement hiddenElement;
    @FindBy(css = "button[id='displayedelement']")
    protected WebElement displayedElement;
    protected WebElement nonExistingElement;
    @FindBy(css = "input[id='textinput']")
    protected WebElement textInput;
    @FindBy(id = "gotonotification")
    protected WebElement notificationDelayButton;
    @FindBy(id = "notifications-header")
    protected WebElement notificationHeader;
    protected By googleLogoBy = new By.ById("hplogo");
    @FindBy(css ="img[id='hplogo']")
    protected WebElement googleLogo;
    @FindBy(css = "a[id='gotoanotherpage']")
    protected WebElement goToAnotherPageButton;
    @FindBy(css = "a[id='displaygotoanotherpagebutton']")
    protected WebElement goToAnotherPageTimerButton;
    @FindBy(css="a[id='hiddengotopagebutton']")
    protected WebElement hiddenGoToPageButton;
    @FindBy(id = "start-rotation-button")
    protected WebElement startRotationButton;
    @FindBy(id="inputelement")
    protected WebElement inputElement;
    @FindBy(id="display-and-enable-element-button")
    protected WebElement displayAndEnableElementButton;
    @FindBy(id="change-attr-value-button")
    protected WebElement changeValueButton;
    @FindBy(id="hidden-disabled-button")
    protected WebElement hiddenDisabledButton;
    @FindBy(id="showchildren")
    protected WebElement showChildrenButton;
    @FindBy(xpath="//ul[@id='parent-ul']")
    protected WebElement parentElement;
    @FindBy(css = "a[href='buttons.html']")
    protected WebElement sideMenuOptionButtons;
    @FindBy(id = "element-one")
    protected WebElement elementOne;
    @FindBy(id = "element-two")
    protected WebElement elementTwo;
    @FindBy(id = "element-three")
    protected WebElement elementThree;
    @FindBy(id = "element-four")
    protected WebElement elementFour;
    @FindBy(id = "element-five")
    protected WebElement elementFive;
    @FindBy(id = "element-six")
    protected WebElement elementSix;
    @FindBy(id = "element-seven")
    protected WebElement elementSeven;
    @FindBy(id = "element-eight")
    protected WebElement elementEight;
    @FindBy(id = "element-nine")
    protected WebElement elementNine;
    @FindBy(id = "element-ten")
    protected WebElement elementTen;
    protected List<WebElement> listOfElements = new ArrayList<WebElement>(){{
            add(elementOne);
            add(elementTwo);
            add(elementThree);
            add(elementFour);
            add(elementFive);
            add(elementSix);
            add(elementSeven);
            add(elementEight);
            add(elementNine);
            add(elementTen);
        }};
    //Must be set manually to number of displayed elements in list that is set in the browser
    protected final int NUMBER_OF_DISPLAYED_ELEMENTS = 6;
    //Must be set manually to number of hidden elements in list that is set in the browser
    protected final int NUMBER_OF_HIDDEN_ELEMENTS = 4;
    protected final String pageSourceFileName = "page_source.xml";

    public MainPage(AutomationDriver driver) {
        super(driver);
    }
    
    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying that the mainpage is visible by seeing if the header is currently shown.");
        if (elementHelper.isElementPresentAndDisplayedWithinTime(pageTitleHeader, 10000)) {
            logger.info("Page-header is displayed");
            return true;
        }
        logger.info("Page-header is not displayed");
        return false;
    }

    public void reloadPage(AutomationDriver driver) {
        initWeb(driver);
    }

    public void initWeb(AutomationDriver driver) {
        driver.navigate().to(BaseTestConfig.getConfigurationOption("url.mainpage"));
        logger.info("We should now be navigating to the Cross Platform QA API Test page");
    }

    public void navigateTo() {
        elementHelper.clickWithinTime(mainPageLink, 2000);
        if(!isPageLoaded()) {
            throw new RuntimeException("Main page did not load correctly, cannot continue test.");
        }
    }

}