package se.soprasteria.automatedtesting.webdriver.api.base;

import org.apache.commons.lang3.tuple.Pair;
import org.testng.ITestContext;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.ConfigUnmarshaller;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.Config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class BaseTestConfig {
    private static BaseTestConfig instance = null;
    private Config config;
    private Map<String, List<Pair<String, Method>>> annotatedMethods = new HashMap<>();
    private Map<String, String> testParameters = new HashMap<>();

    protected BaseTestConfig(String pathToFile) {
        config = ConfigUnmarshaller.getData(pathToFile);
        annotatedMethods.put("Given", new ArrayList<>());
        annotatedMethods.put("When", new ArrayList<>());
        annotatedMethods.put("Then", new ArrayList<>());
    }

    public static BaseTestConfig getInstance() {
        if (instance == null) {
            throw new RuntimeException("You must initialize BaseTestConfig with filepath before using it");
        }
        return instance;
    }

    public static BaseTestConfig getInstance(String pathToFile) {
        if (instance == null) {
            instance = new BaseTestConfig(pathToFile);
        }
        return instance;
    }

    public static BaseTestConfig getInstance(String pathToFile, ITestContext context) {
        if (instance == null) {
            instance = new BaseTestConfig(pathToFile);
        }
        instance.testParameters = context.getCurrentXmlTest().getAllParameters();
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public void addAnnotatedMethod(String type, String regex, Method method) {
        annotatedMethods.get(type).add(Pair.of(regex, method));
    }

    public Method getAnnotatedMethod(String type, String text) throws NullPointerException {
        try {
            Pair result = annotatedMethods.get(type.trim()).stream().filter(x -> Pattern.matches(x.getKey(), text.trim())).findAny().orElse(null);
            return (Method) result.getValue();
        }
        catch (NullPointerException nullpointer) {
            return null;
        }
    }

    public Map<String, List<Pair<String, Method>>> getAnnotatedMethods() {
        return annotatedMethods;
    }

    public static String getConfigurationOption(String propertyName) {
        String property = getInstance().getConfig().getProperty(propertyName);
        if(getInstance().testParameters.containsKey(propertyName)) property = getInstance().testParameters.get(propertyName);
        if(property == null) property = "";
        return property;
    }

}
