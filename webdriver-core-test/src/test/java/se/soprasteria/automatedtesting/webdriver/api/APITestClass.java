package se.soprasteria.automatedtesting.webdriver.api;

import org.testng.annotations.Test;

public class APITestClass extends APITestCase {

    @Test(groups = {"api"})
    public void isPageLoaded() {
        apiTestURL.isPageLoaded().assertThat().statusCode(200);

    }
}
