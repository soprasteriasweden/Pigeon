package se.soprasteria.automatedtesting.webdriver.api.base;


import gherkin.ast.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.springframework.core.annotation.AnnotationUtils;
import org.testng.annotations.BeforeMethod;
import se.soprasteria.automatedtesting.webdriver.api.bdd.Given;
import se.soprasteria.automatedtesting.webdriver.api.bdd.When;
import se.soprasteria.automatedtesting.webdriver.api.bdd.Then;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase.BTCHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.Setup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * TODO Cleanup and comment
 * TODO Fix issues with screenshots (they cant be snapped due to them the listeners/reporter not finding the webdriver because its a field)
 * TODO Fix issue where every test is reported to QAP as bdd
 * TODO Fix regex matching with page objects
 * TODO for this to work in the framework it needs to get instances of the pageobjects
 * TODO Move files
 *
 */
public abstract class BDDBaseTestCase extends BaseTestCase {
    /** The webdriver config to used, this comes from the testfactory in the constructor. */
    protected String bddConfig;
    /** Contains a map of all arguments used by the testmethod, this is used by the testmethods to pickup stories */
    protected Map<String, ScenarioDefinition> scenariosByTestMethod;

    protected void getAnnotatedMethodsFromPages(Class[] classes) {
        BaseTestConfig config = BaseTestConfig.getInstance();

        for (Class page: classes) {
            for (Method method : page.getMethods()) {
                Given given = AnnotationUtils.findAnnotation(method, Given.class);
                When when = AnnotationUtils.findAnnotation(method, When.class);
                Then then = AnnotationUtils.findAnnotation(method, Then.class);

                if (given != null) {
                    config.addAnnotatedMethod("Given", given.regex(), method);
                }

                if (when != null) {
                    config.addAnnotatedMethod("When", when.regex(), method);
                }

                if (then != null) {
                    config.addAnnotatedMethod("Then", then.regex(), method);
                }
            }
        }
        Map<String, List<Pair<String, Method>>> am = config.getAnnotatedMethods();
    }


    @BeforeMethod(alwaysRun = true)
    public void testData(Method method, Object[] testData) throws Exception {
        this.driver = new Setup(BTCHelper.getDriverConfigurations(logger, bddConfig).get(0), testSuiteName, method.getName()).getWebDriver();
        initializeDriver(this.driver);
    }

    public BDDBaseTestCase(String bddConfig, Map<String, ScenarioDefinition> scenariosByTestMethod) {
        this.bddConfig = bddConfig;
        this.scenariosByTestMethod = scenariosByTestMethod;
    }

    public void bdd() throws Exception {
        runStory();
    }

    protected void runStory() throws Exception {
        String callingMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.info("Running story from method: " + callingMethodName);

        ScenarioDefinition scenarioDefinition = scenariosByTestMethod.get(callingMethodName);
        for (Step step : scenarioDefinition.getSteps()) {
            invokeGherkinStep(step);
        }
    }

    private void invokeGherkinStep(Step step) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
        Method method = BaseTestConfig.getInstance().getAnnotatedMethod(step.getKeyword(), step.getText());
        if (method == null) {
            logger.error(String.format("Couldn't find a matching method & class to your given step! Keyword: \"%s\" Text: \"%s\"", step.getKeyword().trim(), step.getText()));
        }

        Object[] args = getStepArguments(step.getText());
        logger.info(String.format("Matching step \"%s\" to method \"%s\" from class \"%s\"", step.getText(), method.getName(), method.getDeclaringClass().getSimpleName()));
        method.invoke(method.getDeclaringClass().getDeclaredConstructor(WebDriver.class).newInstance(driver), args);
    }

    private Object[] getStepArguments(String text) {
        String[] textArray = text.split(" ");
        ArrayList<String> arguments = new ArrayList<>();
        for (String textSegment: textArray) {
            if (Pattern.matches("\"(.*?)\"", textSegment)) {
                arguments.add(textSegment.split("\"")[1]);
            }
        }
        return arguments.toArray();
    }
}


