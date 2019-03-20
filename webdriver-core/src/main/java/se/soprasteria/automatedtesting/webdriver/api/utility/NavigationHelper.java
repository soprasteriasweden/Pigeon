package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.helpers.navigation.Scroller;

public class NavigationHelper {

    private AutomationDriver driver;
    private Logger logger;
    private Scroller scroller;

    public NavigationHelper(Logger logger, AutomationDriver driver) {
        this.logger = logger;
        this.driver = driver;
        this.scroller = new Scroller(driver);
    }

    /**
     * Scrolls the element on which it´s called into the visible area of the browser window.
     *
     * @param webElement      Element on which method is called
     */
    public void scrollToElement(WebElement webElement) {
        scroller.scrollToElement(webElement);
    }

    /**
     * Scrolls to top of page
     */
    public void scrollToTop(){
    scroller.scrollToTop();
    }

    /**
     * Scrolls to bottom of page
     */
    public void scrollToBottom(){
    scroller.scrollToBottom();
    }

}
