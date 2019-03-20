package se.soprasteria.automatedtesting.webdriver.api.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;



public class ErrorsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeMethod
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void shouldOutputErrorToConsole() {
        Logger logger = LogManager.getLogger("testing logging");
        String[] fixes = {"FIX1"};
        Errors.logError(logger, "ERRORMESSAGE", fixes);
        String logOutput = getLogOutput();
        Assert.assertTrue(logOutput.contains("ERROR: ERRORMESSAGE"));
        Assert.assertTrue(logOutput.contains("POSSIBLE FIX: FIX1"));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldThrowExceptionOnNull() {
        Errors.throwExceptionIfNull(null, "Error");
    }

    @Test
    public void shouldNotThrowExceptionOnNull() {
        Errors.throwExceptionIfNull("valid", "Error");
    }

    @Test
    public void shouldConstructValidErrorMessage() {
        String[] fixes = {"FIX1"};
        String errorMessage = Errors.getErrorMessage("ERRORMESSAGE", fixes);
        Assert.assertTrue(errorMessage.contains("ERROR: ERRORMESSAGE"));
        Assert.assertTrue(errorMessage.contains("POSSIBLE FIX: FIX1"));
    }

    private String getLogOutput() {
        try {
            return new String(outContent.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Test invalid");
        }
    }
}
