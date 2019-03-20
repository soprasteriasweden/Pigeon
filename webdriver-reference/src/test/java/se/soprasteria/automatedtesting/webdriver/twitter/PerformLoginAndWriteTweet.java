package se.soprasteria.automatedtesting.webdriver.twitter;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.User;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.twitter.framework.base.TwitterBaseTest;


public class PerformLoginAndWriteTweet extends TwitterBaseTest {


    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"standard"})
    public void performLoginAndWriteTweet(AutomationDriver driver) {
        initPages(driver);
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page failed to load correctly");
        User user = BaseTestConfig.getInstance().getConfig().users.get(0); // will access the id="exampleUser" in twitter_config.xml
        loginPage.performLogin(user);
        Assert.assertTrue(mainPage.isPageLoaded(), "Main page failed to load correctly");
        String tweet = "Hello World!";
        mainPage.performTweet(tweet);
        Assert.assertTrue(mainPage.checkIfTweetHasBeenMade(tweet), "The Tweet failed to be tweeted properly");
        logger.info("Test passed - tweet was performed successfully!");
    }

}
