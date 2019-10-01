package se.soprasteria.automatedtesting.webdriver.api;

import se.soprasteria.automatedtesting.webdriver.api.base.api.ApiBaseTestCase;
import se.soprasteria.automatedtesting.webdriver.api.model.APITestURL;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class APITestCase extends ApiBaseTestCase {

    public APITestURL apiTestURL;

    @Override
    protected String getDriverConfigId() {
        return "api";
    }

    @Override
    protected String getConfigFile() {
        return "config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {
        apiTestURL = new APITestURL();
    }
}
