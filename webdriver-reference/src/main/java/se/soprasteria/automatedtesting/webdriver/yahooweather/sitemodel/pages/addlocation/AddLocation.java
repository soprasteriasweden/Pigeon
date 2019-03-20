package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.addlocation;

import se.soprasteria.automatedtesting.webdriver.api.bdd.When;
import se.soprasteria.automatedtesting.webdriver.api.bdd.Then;


public interface AddLocation {

    @Then(regex = "Is add location page loaded")
    public abstract boolean isPageLoaded();

    @When(regex = "Clear search text")
    public abstract void clearSearch();

    @When(regex = "I add new location as \"(\\D+)\"")
    public abstract boolean addNewLocation(String location);
}