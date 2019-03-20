package se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.mainpage;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import se.soprasteria.automatedtesting.webdriver.api.base.BasePageObject;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.mainpage.MainPage;

public class MainPageWeb extends BasePageObject implements MainPage {


    @FindBy(xpath = "//*[@id=\"tweet-box-home-timeline\"]") // from xpath: //*[@id="tweet-box-home-timeline"]
    protected WebElement tweetBox;
    @FindBy(css = ".tweet-action.EdgeButton.EdgeButton--primary.js-tweet-btn")
    protected WebElement sendBtn;
    @FindBy(css="ol#stream-items-id li:nth-of-type(1) div[class='js-tweet-text-container']")
    protected WebElement newTweet;


    public MainPageWeb (AutomationDriver driver) {
        super(driver);
        defaultWebpageElementLocator(driver);
    }

    public void performTweet(String message) {
        elementHelper.sendKeysWithControlledSpeed(tweetBox, message, 0);
        elementHelper.clickWithinTime(sendBtn, 3000);
        elementHelper.isTextPresentInElementWithinTime(newTweet, message, 4000);
    }

    public boolean checkIfTweetHasBeenMade(String message){
        if(newTweet.getText().equals(message)) {
            logger.info("The text inside of the new tweet box is = " + newTweet.getText() + "... just like its supposed to be");
            return true;
        }
        logger.info("The text inside of the new tweet box is = " + newTweet.getText() + "... which is Wrong");
        return false;
    }

    @Override
    public boolean isPageLoaded() {
        logger.info("Verifying that the tweetBox is visible by seeing if the tweetBox is currently shown.");
        boolean isLogoShown = elementHelper.isElementDisplayedWithinTime(tweetBox, 5000);
        if (isLogoShown) {
            logger.info("The tweetBox is shown, returning true");
            return true;
        }
        logger.info("The tweetBox is not shown, returning false");
        return false;
    }

}
