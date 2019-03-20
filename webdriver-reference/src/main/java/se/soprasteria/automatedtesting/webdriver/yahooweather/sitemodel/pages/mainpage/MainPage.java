package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.mainpage;

import se.soprasteria.automatedtesting.webdriver.api.bdd.Given;
import se.soprasteria.automatedtesting.webdriver.api.bdd.Then;
import se.soprasteria.automatedtesting.webdriver.api.bdd.When;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;


public interface MainPage {
    @Given(regex = "main page is loaded")
    public abstract boolean isPageLoaded();
    @When(regex = "I click add location")
    public abstract void gotoAddLocation();
    public abstract boolean waitForExpectedLocation(String expected, int millis);

    @Then(regex = "current location is (\\D+)")
    public abstract String getCurrentLocation();
    public abstract void swipeLeft();
    public abstract void swipeRight();
    public abstract void swipeUp();
    public abstract void swipeDown();

    public abstract boolean windIndicatorChanged(AutomationDriver driver);
    public abstract boolean hasSiteChanged();
    public abstract String getCurrentTime();
}
