package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.api.utility.Credentials;


public class CredentialsTest {

    private String CONFIG_XML = "config.xml";

    @DataProvider(name = "invalidPaths")
    public Object[][] getIP() {
        return new Object[][]{
                {"just.wrong"},
                {"äåö"},
                {"users"},
                {".xml"},
                {"." + this.getClass().getClassLoader().getResource("").getPath()}
        };
    }

    @Test
    public void shouldContainAValidDummyUser() {
        String configXML = CONFIG_XML;
        BaseTestConfig.getInstance(configXML);
        Credentials credentials = new Credentials();
        Assert.assertEquals(credentials.getId(), "dummy");
        Assert.assertEquals(credentials.getRole(), "dummy");
        Assert.assertEquals(credentials.getUsername(), "badUsername");
        Assert.assertEquals(credentials.getPassword(), "badPassword");
        Assert.assertEquals(credentials.getPin(), "9876");
    }

    @Test(dataProvider = "invalidPaths", expectedExceptions = RuntimeException.class)
    public void shouldNotParseAndThrowException(String invalidFilePath) {
        Credentials credentials = new Credentials(invalidFilePath);
    }

    @Test
    public void shouldBeAbleToAddAUser() {
        BaseTestConfig.getInstance(CONFIG_XML);
        Credentials credentials = new Credentials();
        credentials.addUser("TESTUSER_ID", "role", "username", "pw", "pin");
        Assert.assertEquals(credentials.getRole(), "dummy");
        Assert.assertEquals(credentials.getRole("TESTUSER_ID"), "role");
        Assert.assertEquals(credentials.getUsername("TESTUSER_ID"), "username");
        Assert.assertEquals(credentials.getPassword("TESTUSER_ID"), "pw");
        Assert.assertEquals(credentials.getPin("TESTUSER_ID"), "pin");
    }
}
