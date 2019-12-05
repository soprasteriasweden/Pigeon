package se.soprasteria.automatedtesting.webdriver.mobile.android;

import org.testng.Assert;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.android.AndroidBaseTestCase;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

public class TextHelperTest extends AndroidBaseTestCase {
    final String NUMBERS = "0123456789";
    final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvxyzåäö";
    final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVXYZÅÄÖ";
    final String SPECIAL_CHARACTERS = "!   #¤%&/()=?`@£${[]}/*-+|";
    final String APOSTROPHE_CHARACTERS = "áéâêüàè";


    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void textTestNumbers(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(NUMBERS);
        Assert.assertEquals(NUMBERS, textPage.getText());
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void textTestLowerCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(LOWERCASE_LETTERS);
        Assert.assertEquals(LOWERCASE_LETTERS, textPage.getText());
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void textTestUpperCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(UPPERCASE_LETTERS);
        Assert.assertEquals(UPPERCASE_LETTERS, textPage.getText());
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void textTestSpecialCharacters(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(SPECIAL_CHARACTERS);
        Assert.assertEquals(SPECIAL_CHARACTERS, textPage.getText());
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void enterPasswordTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterPassword(LOWERCASE_LETTERS);
    }


    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void textTestApostropheCaseLetters(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(APOSTROPHE_CHARACTERS);
        Assert.assertEquals(APOSTROPHE_CHARACTERS, textPage.getText());
    }

    @Test(timeOut = 180000, dataProvider = "getDriver", groups = {"android"})
    protected void clearTest(AutomationDriver driver) {
        Assert.assertTrue(elementPage.isPageLoaded(), "App failed to load correctly");
        bottomNavigation.setNavigateTextPage();
        Assert.assertTrue(textPage.isPageLoaded(), "Text test page failed to load correctly");
        textPage.enterText(LOWERCASE_LETTERS);
        textPage.clearText();
        textPage.enterText(NUMBERS);
        Assert.assertEquals(NUMBERS, textPage.getText());
    }

}
