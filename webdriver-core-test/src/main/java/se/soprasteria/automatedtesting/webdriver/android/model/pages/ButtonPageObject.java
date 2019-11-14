package se.soprasteria.automatedtesting.webdriver.android.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class ButtonPageObject extends BasePageObject {

    @FindBy(id = "resetButton")
    WebElement buttonResetAllElements;
    @FindBy(id = "switchToSwipe")
    WebElement buttonSwitchToSwipeScreen;
    @FindBy(id = "showElement")
    WebElement buttonShowElement;
    @FindBy(id = "shownElement")
    WebElement textShownElement;
    @FindBy(id = "showElementDelay")
    WebElement buttonShowElementAfterDelay;
    @FindBy(id = "shownElementDelay")
    WebElement textShownElementAfterDelay;
    @FindBy(id = "hideElementDelay")
    WebElement buttonHideElementAfterDelay;
    @FindBy(id = "hiddenElementDelay")
    WebElement textHiddenElementAfterDelay;
    @FindBy(id = "removeElementDelay")
    WebElement buttonRemoveElementAfterDelay;
    @FindBy(id = "removedElementDelay")
    WebElement textRemovedElementAfterDelay;

    public ButtonPageObject(AutomationDriver driver) {
        super(driver);
        defaultWebpageElementLocator(driver);
        PageFactory.initElements(driver.getAndroidDriver(), this);
    }

    @Override
    public boolean isPageLoaded() {
        return elementHelper.isElementDisplayedWithinTime(buttonResetAllElements, 60000);
    }


    public void switchToSwipeScreen() {
        elementHelper.clickWithinTime(buttonSwitchToSwipeScreen, 2000);
    }

}
