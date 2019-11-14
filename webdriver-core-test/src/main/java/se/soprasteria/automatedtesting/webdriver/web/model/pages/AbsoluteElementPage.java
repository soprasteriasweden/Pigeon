package se.soprasteria.automatedtesting.webdriver.web.model.pages;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.WebBasePageObject;

import java.util.List;

public class AbsoluteElementPage extends WebBasePageObject {

    @FindBy(css = "h1[class='page-header']")
    protected WebElement pageTitleHeader;
    @FindBy(id = "absoluteelement")
    protected WebElement absolutelement;
    protected Point elementsLocation,locationFromCSS;

    public AbsoluteElementPage(AutomationDriver driver) {
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

    public void initWeb(AutomationDriver driver) {
        driver.navigate().to(BaseTestConfig.getConfigurationOption("url.absolutepositionpage"));
        logger.info("We should now be navigating to the Absolute positioned element page");
    }

    public void getElementsCSSLocation() {
        String cssXValue = absolutelement.getCssValue("left").replace("px","");
        String cssYValue = absolutelement.getCssValue("top").replace("px","");
        int x = Integer.parseInt(cssXValue);
        int y = Integer.parseInt(cssYValue);
        locationFromCSS = new Point(x,y);
    }

    public void getElementLocationInViewPort() {
        elementsLocation = elementHelper.getElementLocationInViewport(absolutelement);
    }

    public boolean isPointsEqual() {
        return (elementsLocation.getX() == locationFromCSS.getX() && elementsLocation.getY() == locationFromCSS.getY());
    }

    public void navigateTo() {
        elementHelper.clickWithinTime(absolutePositionPageLink, 2000);
        if(!isPageLoaded()) throw new RuntimeException("Absolute Element page did not load correctly, cannot continue test.");
    }

}