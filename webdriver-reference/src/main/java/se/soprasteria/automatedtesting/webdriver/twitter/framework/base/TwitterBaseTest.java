package se.soprasteria.automatedtesting.webdriver.twitter.framework.base;


import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.TwitterPageFactory;
import se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.loginpage.LoginPage;
import se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.mainpage.MainPage;


public class TwitterBaseTest extends BaseTestCase {

	protected MainPage mainPage;
	protected LoginPage loginPage;

	public TwitterBaseTest() {
		super();
	}

	protected void initPages(AutomationDriver driver) {
		logger.info("Initialising pages to be used in test");
		loginPage = TwitterPageFactory.getLoginPage (driver);
		mainPage = TwitterPageFactory.getMainPage(driver);
	}

	@Override
	protected void initializeDriver(AutomationDriver driver) {
		if (driver.isWeb()) {
			driver.navigate().to("https://twitter.com/login");
			driver.manage().window().maximize();
		} else if (driver.isAndroid()){
			driver.navigate().to("https://mobile.twitter.com/login");
		} else if (driver.isIos()){
			driver.navigate().to("https://mobile.twitter.com/login");
		}
		sleep(1000);
		logger.info("Navigated to Twitter's login-page, sleeping for 1s to allow it to initiliaze");
	}

	@Override
	protected String getDefaultPropertyFile() {
		return "twitter/twitter_config.xml";
	}

}
