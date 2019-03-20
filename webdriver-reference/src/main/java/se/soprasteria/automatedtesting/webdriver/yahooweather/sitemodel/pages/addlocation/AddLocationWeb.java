package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class AddLocationWeb  extends BasePageObject implements AddLocation {
    @FindBy(xpath = "//input[contains(@placeholder,'Enter City or ZIP code')]")
    protected WebElement searchField;
    @FindBy(xpath = "//ul[contains(@class,'location ')]")
    protected WebElement resultBox;

    protected By resultItems = By.xpath("//ul[contains(@class,'location ')]/li");

    public AddLocationWeb(AutomationDriver webDriver) {
        super(webDriver);
        defaultWebpageElementLocator(webDriver);
    }

    public boolean addNewLocation(String location) {
        logger.info("Adding location by entering text into editbox, location to add: " + location);
        elementHelper.waitForElementToBePresentAndDisplayed(searchField, 2000);
        elementHelper.sendKeysWithControlledSpeed(searchField, location, 200);
        searchField.sendKeys(Keys.ENTER);
        sleep(1000);
        return true;

    }

    public void clearSearch() {
        logger.info("Clearing search by pressing the escape key in the searchfield");
        searchField.sendKeys(Keys.ESCAPE);
    }

    public boolean isPageLoaded() {
        boolean loaded = elementHelper.isElementPresentAndDisplayed(searchField);
        logger.info(String.format("Verifying page is loaded by checking if the searchfield is displayed: %b", loaded));
        return loaded;
    }

}