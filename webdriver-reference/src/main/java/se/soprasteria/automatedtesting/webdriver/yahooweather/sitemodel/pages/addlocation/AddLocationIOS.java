package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public class AddLocationIOS extends BasePageObject implements AddLocation {

    @FindBy(css = ".search-input")
    protected WebElement searchField;
    @FindBy(css = "button[aria-label='Cancel']")
    protected WebElement searchResetButton;
    @FindBy(xpath = "/html/body/div[1]/div/div[1]/div/div/div/div/div/div/div[1]/div/div/div/div[2]/div/section[1]/div[2]/div/div/section/ul")
    protected WebElement resultBox;

    protected By resultItem = By.cssSelector("li");


    public AddLocationIOS (AutomationDriver driver) {
        super(driver);
        defaultIOSElementLocator(driver);
    }

    public boolean isPageLoaded() {
        logger.info("Checking to see if the add location page is loaded by verifying if the search field is displayed");
        return elementHelper.isElementDisplayedWithinTime(searchField, 3000);
    }

    /*   Actions   */
    public void clearSearch() {
        logger.info("Clearing search by clicking search reset button");
        searchResetButton.click();
    }

    //TODO Currently not working due to a sendKeyStrategy bug in Appium where it send keys as setValue and not oneByOne
    public boolean addNewLocation(String location) {
        logger.info("Adding new location by entering text into searchfield, location: " + location);
        searchField.click();
        searchField.sendKeys(location);
        if (resultBox.findElements(resultItem).isEmpty()) {
            logger.info("No results were found, navigating out of search page by pressing back twice and returning false");
            driver.navigate().back();
            driver.navigate().back();
            return false;
        }
        logger.info("Found some results, sleeping for 2 seconds then choose the first result no matter what the text and returning true");
        resultBox.findElements(resultItem).get(0).click();

        return true;
    }

}
