package se.soprasteria.automatedtesting.mattiastest;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;
import se.soprasteria.automatedtesting.webdriver.mattiastest.NativeBaseTest;

public class TextTest extends NativeBaseTest {

    final String NUMBERS = "0123   456789";
    final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvxyzåäö";
    final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVXYZÅÄÖ";
    final String SPECIAL_CHARACTERS = " !#¤%&/()=?`@£${[]}/*-+|";
    final String APOSTROPHE_CHARACTERS = "áéâêüàè";


    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void textTestNumbers(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText(NUMBERS);
        Assert.assertEquals(mainPage.getText(), NUMBERS);
        sleep(1000);
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void textTestLowerCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText(LOWERCASE_LETTERS);
        Assert.assertEquals(mainPage.getText(), LOWERCASE_LETTERS);
        sleep(1000);
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void textTestUpperCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText(UPPERCASE_LETTERS);
        Assert.assertEquals(mainPage.getText(), UPPERCASE_LETTERS);
        sleep(1000);
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void textTestSpecialCharacters(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText(SPECIAL_CHARACTERS);
        Assert.assertEquals(mainPage.getText(), SPECIAL_CHARACTERS);
        sleep(1000);
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    protected void enterPasswordTest(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterPassword(LOWERCASE_LETTERS);
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"mobile"})
    protected void clearTest(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText("123456");
        mainPage.clearText();
        mainPage.enterText("abcdefg");
        Assert.assertEquals("abcdefg", mainPage.getText());
    }

    @Test(timeOut = 180000, dataProvider="getDriver", groups = {"mobile"})
    protected void textTestApostropheCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(mainPage.isLoaded(), "App did not load correctly");
        mainPage.enterText(APOSTROPHE_CHARACTERS);
        Assert.assertEquals(mainPage.getText(), APOSTROPHE_CHARACTERS);
        sleep(1000);
    }

}
