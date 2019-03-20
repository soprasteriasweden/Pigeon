package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.utility.Data;


public class DataTest {


    @Test
    public void shouldContainString() {
        Assert.assertTrue(Data.stringContains("cat cat cat cattie cat", "\\bcat\\b"));
    }

    @Test
    public void shouldNotContainString() {
        Assert.assertFalse(Data.stringContains("cat cat cat cattie cat", "\\bcatcat\\b"));
        Assert.assertFalse(Data.stringContains("cat cat cat cattie cat", "\\bdog\\b"));
    }

    @Test
    public void shouldMatchString() {
        Assert.assertTrue(Data.stringMatches("cat", "\\bcat\\b"));
        Assert.assertTrue(Data.stringMatches("cat cat cat cattie cat", "\\bcat cat cat cattie cat\\b"));
    }

    @Test
    public void shouldNotMatchString() {
        Assert.assertFalse(Data.stringMatches("cat cat cat cattie cat", "\\bcat\\b"));
        Assert.assertFalse(Data.stringMatches("cat cat cat cattie cat", null));
    }

    @Test
    public void shouldContainCorrectNumberOfGroups() {
        Assert.assertEquals(Data.stringGetGroups("cat cat cat cattie cat", "\\bcat\\b").size(),1);
    }

    @Test
    public void shouldContainAValidTime() {
        Assert.assertTrue(Data.stringContainsVerbalTime("12 hours 12 minuter"));
    }

    @Test
    public void shouldNotContainAValidTime() {
        Assert.assertFalse(Data.stringContainsVerbalTime("12 hours 12 mnuter"));
    }

    @Test
    public void shouldDisectStringCorrectly() {
        Assert.assertEquals(Data.stringRemoveRegex("cat cat cat cattie cat", "\\bcat\\b"), "   cattie ");
    }

}
