package se.soprasteria.automatedtesting.webdriver.helpers.utility;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class AppiumLog {


    private static final String APPIUM_LOG = "appium_log";
    private static boolean printLog = false;
    private static File appiumLog;
    private static List<String> appiumLogLines;
    private static int currentPlacementInLog = 0;

    public static void set(Logger logger, boolean print) {
        printLog = print;
        if(printLog) {
            fetchAppiumLog(logger);
        }
    }

    public static void printAppiumLog(Logger logger) {
        if(printLog) {
            extractAppiumLogLines(logger);
            printLog(logger);
        }
    }

    public static void printAppiumLogForCurrentTest(ITestResult testResult, Logger logger){
        if(printLog) {
            extractAppiumLogLines(logger);
            printLinesForCurrentTest(testResult, logger);
        }
    }

    /**
     * Fetch an appium log from a readable text file and converts it to File io format
     * The appium log is specified to be contained in the surefire-reports folder and
     * to have a file name starting with "appium_log". It grabs the first matching file
     * (more than one appium log should not exist in the folder)
     * */
    private static void fetchAppiumLog(Logger logger){
        try{
            appiumLog = new File(System.getProperty("user.dir") + "/").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File pathName, String name) {
                    return name.startsWith(APPIUM_LOG);
                };
            })[0];
        } catch (Exception e) {
            throw new RuntimeException("Could not find appium log file, make sure you have enabled appium logging by using appium server " +
                    "argument --log with the path to the root of the project and specify the appium log filename to start with " +
                    "\"appium_log\" when starting appium. Example: \"--log c://<projectDirectoryPath>/appium_log_MyAppiumLog.log\"");
        }
    }

    /**
     * Converts each line of text to a String and puts the lines into a List
     * */
    private static void extractAppiumLogLines(Logger logger){
        appiumLogLines = new ArrayList<String>();
        try{
            appiumLogLines = FileUtils.readLines(appiumLog, "utf-8");
        } catch (Exception e) {
            logger.warn("Could not extract lines from appium log file. Unable to read log file with encoding utf-8, make sure appium log file exists and is encoded using utf-8");
        }
    }

    private static void printLog(Logger logger) {
        for(String line : appiumLogLines) {
            logger.debug("APPIUM LOG: " + line);
        }
    }

    /**
     * Prints out each line until the start of the first test
     * */
    private static void printAppiumInitialization(Logger logger) {
        for(String line : appiumLogLines) {
            if(line.contains("newSessionRequested")) break;
            logger.debug("APPIUM INITIALIZATION: " + line);
        }
    }

    /**
     * Searches for the test case matching the name of the testResults,
     * if it's the first test in the suite the initialization part
     * of the appium log is also printed. The start and end of each test
     * section of the log is identified by "newSessionRequested" and
     * "quitSessionFinished".
     * */
    private static void printLinesForCurrentTest(ITestResult testResult, Logger logger){
        boolean readingLog = true;
        boolean searching = true;
        boolean printing = false;
        int currentLineNr = currentPlacementInLog;

        try {
            while(readingLog) {
                String currentLine = appiumLogLines.get(currentLineNr);

                if(currentLineNr == 0) printAppiumInitialization(logger);

                if(searching && currentLine.contains(testResult.getName())) {
                    while(searching) {
                        if(currentLine.contains("newSessionRequested")) {
                            searching = false;
                            printing = true;
                        } else {
                            currentLineNr++;
                            currentLine = appiumLogLines.get(currentLineNr);
                        }
                    }
                } else { currentLineNr++; }

                if(printing) {
                    if(currentLine.length() != 0){ logger.debug("APPIUM LOG \"" + testResult.getName() +"\": " + currentLine); }
                    if(currentLine.contains("quitSessionFinished")) {
                        readingLog = false;
                        currentPlacementInLog = currentLineNr;
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Could not find the current test in the appium log, remove the appium log file located at \"c:/<projectDirectoryPath>/appium_log_MyAppiumLog.log\" and try again");
        }
    }
}