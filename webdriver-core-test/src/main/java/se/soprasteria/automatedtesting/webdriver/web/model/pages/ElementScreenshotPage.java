package se.soprasteria.automatedtesting.webdriver.web.model.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.web.WebBasePageObject;
import se.soprasteria.automatedtesting.webdriver.web.datastructure.ScreenshotElement;

public class ElementScreenshotPage extends WebBasePageObject {

    @FindBy(css = "h1[class='page-header']")
    protected WebElement pageTitleHeader;
    @FindBy(id = "rect")
    protected WebElement imageRect;
    @FindBy(id = "imagefilled0-3")
    protected WebElement fillRectButton0_3;
    @FindBy(id = "imagefilled1-3")
    protected WebElement fillRectButton1_3;
    @FindBy(id = "imagefilled2-3")
    protected WebElement fillRectButton2_3;
    @FindBy(id = "imagefilled3-3")
    protected WebElement fillRectButton3_3;
    @FindBy(id = "rotationdiv")
    protected WebElement rotationDiv;
    @FindBy(id = "color1")
    protected WebElement coloredElement1;
    @FindBy(id = "color2")
    protected WebElement coloredElement2;
    @FindBy(id = "color3")
    protected WebElement coloredElement3;
    @FindBy(id = "color4")
    protected WebElement coloredElement4;
    @FindBy(id = "color5")
    protected WebElement coloredElement5;
    @FindBy(id = "resetcolors")
    protected WebElement resetColorsButton;

    public ElementScreenshotPage(AutomationDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying that the striped div is visible");
        if (elementHelper.isElementPresentAndDisplayedWithinTime(pageTitleHeader, 10000)) {
            logger.info("Striped div is displayed");
            return true;
        }
        logger.info("Striped div is not displayed");
        return false;
    }

    public void reloadPage(AutomationDriver driver) {
        initWeb(driver);
    }

    public void initWeb(AutomationDriver driver) {
        if (!driver.isMobile())
            driver.navigate().to(BaseTestConfig.getConfigurationOption("url.elementscreenshotpage"));
        logger.info("We should now be navigating to the Element screenshot page");
    }

    public void clickImageRect(){
        elementHelper.clickWithinTime(imageRect, 2000);
    }

    public void clickRectButton0_3(){
        elementHelper.clickWithinTime(fillRectButton0_3, 2000);
    }

    public void clickRectButton1_3(){
        elementHelper.clickWithinTime(fillRectButton1_3, 2000);
    }

    public void clickRectButton2_3(){
        elementHelper.clickWithinTime(fillRectButton2_3, 2000);
    }

    public void clickRectButton3_3(){
        elementHelper.clickWithinTime(fillRectButton3_3, 2000);
    }

    public void clickResetColors(){
        elementHelper.clickWithinTime(resetColorsButton,2000);
    }

    public boolean clickColorButton(ScreenshotElement element){
        switch(element){
            case COLORED_ELEMENT_1:
                return elementHelper.clickWithinTime(coloredElement1,2000);
            case COLORED_ELEMENT_2:
                return elementHelper.clickWithinTime(coloredElement2,2000);
            case COLORED_ELEMENT_3:
                return elementHelper.clickWithinTime(coloredElement3,2000);
            case COLORED_ELEMENT_4:
                return elementHelper.clickWithinTime(coloredElement4,2000);
            case COLORED_ELEMENT_5:
                return elementHelper.clickWithinTime(coloredElement5,2000);
            default:
                return false;
        }
    }

    public void clickStartRotationButton(){
        elementHelper.clickWithinTime(rotationDiv,2000);
    }

    public boolean didElementChangeDuringInterval(){
        navigationHelper.scrollToElement(rotationDiv);
        return elementHelper.didElementChangeDuringInterval(rotationDiv, 2000);
    }

    public boolean didElementChangeDuringInterval(ScreenshotElement element, float threshold){
        switch (element) {
            case ROTATION_ELEMENT:
                return elementHelper.didElementChangeDuringInterval(rotationDiv,threshold,2000);
            case BLACK_RECT_ELEMENT:
                return elementHelper.didElementChangeDuringInterval(imageRect,threshold,2000);
            case COLORED_ELEMENT_1:
                return elementHelper.didElementChangeDuringInterval(coloredElement1,threshold,2000);
            case COLORED_ELEMENT_2:
                return elementHelper.didElementChangeDuringInterval(coloredElement2,threshold,2000);
            case COLORED_ELEMENT_3:
                return elementHelper.didElementChangeDuringInterval(coloredElement3,threshold,2000);
            case COLORED_ELEMENT_4:
                return elementHelper.didElementChangeDuringInterval(coloredElement4,threshold,2000);
            case COLORED_ELEMENT_5:
                return elementHelper.didElementChangeDuringInterval(coloredElement5,threshold,2000);
            default:
                return false;
        }
    }

    public void navigateTo() {
        elementHelper.clickWithinTime(screenshotPageLink, 2000);
        if (!isPageLoaded()) {
            throw new RuntimeException("Element Screenshot page did not load correctly, cannot continue test.");
        }
    }
}
