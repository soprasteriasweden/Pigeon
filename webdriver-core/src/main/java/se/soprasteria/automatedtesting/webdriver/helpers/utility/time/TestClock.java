package se.soprasteria.automatedtesting.webdriver.helpers.utility.time;

import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TestClock {

    private static Map<Long, String> testStartTimes = new HashMap<>();

    public static String getCurrentTestStartTime(ITestResult result) {
        return getTestStartTime(result.getStartMillis());
    }

    public static String getTestStartTime(Long testID){
        if(testStartTimes.containsKey(testID)) {
            return testStartTimes.get(testID);
        } else {
            String testStartTime = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss").format(LocalDateTime.now());
            testStartTimes.put(testID, testStartTime);
            return testStartTime;
        }
    }

}
