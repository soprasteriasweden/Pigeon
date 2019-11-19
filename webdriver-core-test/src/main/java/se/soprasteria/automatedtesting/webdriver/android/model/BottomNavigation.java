package se.soprasteria.automatedtesting.webdriver.android.model;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class BottomNavigation extends BasePageObject {

    @FindBy(id = "nav_elements")
    WebElement navigateElementPage;
    @FindBy(id = "nav_swipe")
    WebElement navigateSwipePage;
    @FindBy(id = "nav_text")
    WebElement navigateTextPage;

    static final int TIMEOUT_MILLIS = 5000;


    public BottomNavigation(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementPresentAndDisplayedWithinTime(navigateElementPage, TIMEOUT_MILLIS);
    }

    public void navigateElementPage() {
        logger.info("Navigating to element page");
        elementHelper.clickWithinTime(navigateElementPage, TIMEOUT_MILLIS);
    }

    public void navigateSwipePage() {
        logger.info("Navigating to swipe page");
        elementHelper.clickWithinTime(navigateSwipePage, TIMEOUT_MILLIS);
    }

    public void setNavigateTextPage() {
        logger.info("Navigating to text page");
        elementHelper.clickWithinTime(navigateTextPage, TIMEOUT_MILLIS);
    }

}
