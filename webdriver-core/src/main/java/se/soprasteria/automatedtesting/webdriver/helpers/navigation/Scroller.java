package se.soprasteria.automatedtesting.webdriver.helpers.navigation;

import org.openqa.selenium.WebElement;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class Scroller extends BaseClass {

    private final AutomationDriver driver;

    public Scroller(AutomationDriver driver) {
        this.driver = driver;
    }

    public void scrollToElement(WebElement webElement){
        driver.executeJavaScript("arguments[0].scrollIntoView(true);", webElement);
    }

    public void scrollToTop(){
        driver.executeJavaScript("window.scrollTo(0, 0)");
    }

    public void scrollToBottom(){
        driver.executeJavaScript("window.scrollTo(0,document.body.scrollHeight)");
    }

}
