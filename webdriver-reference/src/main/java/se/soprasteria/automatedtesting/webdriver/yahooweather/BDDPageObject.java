package se.soprasteria.automatedtesting.webdriver.yahooweather;

import org.testng.Assert;
import se.soprasteria.automatedtesting.webdriver.api.bdd.When;
import se.soprasteria.automatedtesting.webdriver.api.bdd.Then;


public class BDDPageObject {

    @When(regex = "execute")
    public void doStuff() {
        System.out.println("This should print");
    }

    @When(regex = "don't execute")
    public void doSomethingElse() {
        System.out.println("This should NOT print");
    }

    @Then(regex = "it went ok", value = true)
    public void checkSomething(boolean value) {
        boolean somethingToCheckOnThePage = true;
        System.out.println("Checked something");
        Assert.assertEquals(somethingToCheckOnThePage, value);
    }
}
