package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.utility.SystemManagement;

import java.io.InputStream;


public class SystemManagementTest {

    @DataProvider(name = "validResources")
    public Object[][] getVR() {
        return new Object[][]{
                {"users.xml"},
                {"/users.xml"},
                {""},
                {"/"},
        };
    }


    @DataProvider(name = "validFiles")
    public Object[][] getVF() {
        return new Object[][]{
                {"users.xml"},
                {"/users.xml"},
        };
    }

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

    @DataProvider(name = "invalidResources")
    public Object[][] getIR() {
        return new Object[][]{
                {"just.wrong"},
                {"äåö"},
                {"users"},
                {".xml"},
                {System.getProperty("user.dir")},
                {this.getClass().getClassLoader().getResource("").getPath()}
        };
    }

    @DataProvider(name = "validPaths")
    public Object[][] getVP() {
        return new Object[][]{
                {System.getProperty("user.dir")},
                {"/"},
                {this.getClass().getClassLoader().getResource("").getPath()}
        };
    }

    @Test(dataProvider = "validPaths")
    public void shouldBeValidPaths(String validPath) {
        boolean isValidPath = SystemManagement.isValidPath(validPath);
        Assert.assertNotNull(isValidPath);
        Assert.assertTrue(isValidPath);
    }

    @Test(dataProvider = "invalidPaths")
    public void shouldNotBeValidPaths(String invalidPath) {
        boolean isValidPath = SystemManagement.isValidPath(invalidPath);
        Assert.assertNotNull(isValidPath);
        Assert.assertFalse(isValidPath);
    }

    @Test(dataProvider = "invalidResources", expectedExceptions = RuntimeException.class)
    public void shouldNotFindResourceAndThrowException(String invalidResource) {
        String file = SystemManagement.resourceGetAbsolutePath(invalidResource);
    }

    @Test(dataProvider = "invalidResources", expectedExceptions = RuntimeException.class)
    public void shouldNotReturnValidStreamFromResource(String invalidPath) {
        InputStream stream = SystemManagement.resourceGetAsStream(invalidPath);
        Assert.assertNull(stream);
    }

    @Test(dataProvider = "validResources")
    public void shouldReturnValidResource(String validResource) {
        String file = SystemManagement.resourceGetAbsolutePath(validResource);
        Assert.assertNotNull(file);
        Assert.assertTrue(SystemManagement.isValidPath(file));
    }

    @Test(dataProvider = "validFiles")
    public void shouldReturnValidStreamFromResource(String validResource) {
        InputStream stream = SystemManagement.resourceGetAsStream(validResource);
        Assert.assertNotNull(stream);
    }
}
