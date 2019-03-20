package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import static se.soprasteria.automatedtesting.webdriver.api.datastructures.Direction.*;
import static se.soprasteria.automatedtesting.webdriver.api.datastructures.Speed.MEDIUM;


public class MainPageIOS extends BasePageObject implements MainPage {

    @FindBy(xpath = "/html/body/div[1]/div/div[1]/div/div/div/div/div/div/div[1]/div/div/div/div[2]/div/section[1]/div[2]/div/button")
    protected WebElement addLocation;
    @FindBy(xpath = "//*[@id=\"Col1-0-Weather\"]/div/section[1]/div[1]/div/h1")
    protected WebElement curLocationText;
    @FindBy(xpath = "//*[@id=\"Col1-0-Weather\"]/div/section[1]/div[3]/span")
    protected WebElement curLocationTime;
    @FindBy(css = "[data-reactid='571']")
    protected WebElement windIndicator;

    public MainPageIOS (AutomationDriver driver){
        super(driver);
        defaultIOSElementLocator(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(curLocationText, 3000);
    }

    public String getCurrentLocation() {
        return curLocationText.getText();
    }

    public String getCurrentTime() {
        return curLocationTime.getText();
    }

    /*   Actions   */
    public void gotoAddLocation() {
        addLocation.click();
    }

    public boolean waitForExpectedLocation(String expected, int millisToWait) {
        elementHelper.waitForTextToBePresentInElement(curLocationText, expected, millisToWait);
        return true;
    }
    /*   Helper functions   */
    public void swipeDown() {
        driver.swipe(DOWN, 50, 90, MEDIUM);
    }

    public void swipeUp() {
        driver.swipe(UP, 50, 90, MEDIUM);
    }

    public void swipeLeft() {
        driver.swipe (LEFT, 95, 95, MEDIUM);
    }

    public void swipeRight() {
        driver.swipe(RIGHT, 95, 95, MEDIUM);
    }

    public boolean hasSiteChanged() {
        driver.executeJavaScript("arguments[0].scrollIntoView(true);", windIndicator);
        return driver.didScreenChangeDuringInterval(2000);
    }

    public boolean windIndicatorChanged(AutomationDriver driver){return false;}

}
