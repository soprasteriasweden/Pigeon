package se.soprasteria.automatedtesting.webdriver.web.model.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.WebBasePageObject;

public class ScrollPage extends WebBasePageObject {

    @FindBy(css = "h1[class='page-header']")
    protected WebElement pageTitleHeader;
    @FindBy(css = "div[id='scrolldestinationelement']")
    protected WebElement scrollDestinationElement;

    public ScrollPage(AutomationDriver driver) {
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

    public void initWeb(WebDriver driver) {
        driver.navigate().to(BaseTestConfig.getConfigurationOption("url.scrollpage"));
        logger.info("We should now be navigating to the Scroll page");
    }

    public void scrollToElement() {
        navigationHelper.scrollToElement(scrollDestinationElement);
    }

    public void scrollToTop() {
        navigationHelper.scrollToTop();
    }

    public boolean isElementVisibleWithinViewport() {
        return elementHelper.isElementVisibleWithinViewport(scrollDestinationElement);
    }

    public void navigateTo() {
        elementHelper.clickWithinTime(scrollPageLink, 2000);
        if(!isPageLoaded()) {
            throw new RuntimeException("Scroll page did not load correctly, cannot continue test.");
        }
    }

}