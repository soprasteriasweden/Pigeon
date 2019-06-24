package se.soprasteria.automatedtesting.webdriver.api.base;

import gherkin.ast.*;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.WordUtils;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import se.soprasteria.automatedtesting.webdriver.api.utility.Data;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.bdd.BDDHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.base.basetestcase.BTCHelper;
import se.soprasteria.automatedtesting.webdriver.helpers.driver.AutomationDriver;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Abstract class to generate the tests from the stories. This class parses all the stories supplied in the parameter
 * story in the TestNG XML an creates single testcase instances of each of them along with a webdriver for it.
 * <p>
 * This class runs before beforeSuite, beforeClass and beforeMethod so it is necessary to initialize all the configs
 * required to generate webdrivers here.
 * <p>
 * TODO - Add support for multiple webdrivers
 */
public class BDDFactory extends BaseTestCase {


    private List<Feature> getFeaturesRecursively(String path) {
        List<Feature> features = new ArrayList<>();
        File folder;

        try {
            folder = new File(this.getClass().getClassLoader().getResource(path).getPath());
        } catch (Exception e) {
            String[] fixes = {"Update the TestNG parameter 'featureFolder' with a valid path"};
            Errors.logError(logger, "The feature file was either missing or corrupt: " + path, fixes);
            return features;
        }

        if (!folder.isDirectory()) {
            String[] fixes = {"Update the parameter 'featureFolder' to point to a folder containing valid feature files"};
            Errors.logError(logger, "The feature file path was not a folder:  " + path, fixes);
            return features;
        }


        Collection<File> files = FileUtils.listFiles(
                folder,
                new RegexFileFilter("^(.*feature?)"),
                DirectoryFileFilter.DIRECTORY
        );

        for (File featureFile : files) {
            Feature feature = BDDHelper.getFeature(featureFile);
            if (feature == null) {
                String[] fixes = {"Fix the file at the path " + featureFile.getAbsolutePath()};
                Errors.logError(logger, "The feature file was corrupt: " + featureFile.getAbsolutePath(), fixes);
                continue;
            }
            features.add(feature);
        }

        return features;
    }

    private CtMethod getBDDBaseMethod(CtClass baseBDDClass) {
        Object[] bddMethods = Arrays.stream(baseBDDClass.getMethods()).filter(x -> x.getName().contains("bdd")).toArray();
        if (bddMethods.length != 1) {
            throw new RuntimeException("An incorrect number of bdd methods found");
        }
        CtMethod bdd = (CtMethod)bddMethods[0];
        return bdd;
    }

    private CtClass getBDDBaseClass(Feature feature, String bddResultPackageName, String baseTestCase) {
        ClassPool classPool = javassist.ClassPool.getDefault();
        String featureNameAsUppercase = WordUtils.capitalizeFully(feature.getName(), new char[]{'_', ' '}).replace(" ", "");
        CtClass baseBDDClass = classPool.makeClass(bddResultPackageName + "." + featureNameAsUppercase);
        try {
            baseBDDClass.setSuperclass(classPool.get(baseTestCase));
        } catch (Exception e) {
            throw new RuntimeException("Could not set superclass to: " + baseTestCase);
        }
        return baseBDDClass;
    }

    private AttributeInfo getAttributeInfoForTestAnnotation(CtClass baseBDDClass) {
        ClassFile ccFile = baseBDDClass.getClassFile();
        ConstPool constpool = ccFile.getConstPool();
        Annotation testAnnotation = new Annotation("org.testng.annotations.Test", constpool);
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        attr.addAnnotation(testAnnotation);
        return attr;
    }

    @Parameters({"propertiesFile", "configurationId", "debugLevel", "featureFolder", "bddBaseTestcase", "bddResultPackageName"})
    @Factory
    public Object[] factoryMethod(@Optional("") String propertiesFile,
                                  @Optional("") String configurationId,
                                  @Optional("") String debugLevel,
                                  @Optional("") String featureFolder,
                                  @Optional("") String baseTestCase,
                                  @Optional("unspecified") String bddResultPackageName) {

        this.configurationId = Data.ifEmptyOverride(logger, configurationId, getDriverConfigId());
        this.propertiesFile = Data.ifEmptyOverride(logger, propertiesFile, getConfigFile());
        this.config = BaseTestConfig.getInstance(this.propertiesFile);

        featureFolder = Data.ifEmptyOverride(logger, featureFolder, getDefaultFolderContainingFeatureFiles());
        baseTestCase = Data.ifEmptyOverride(logger, baseTestCase, getDefaultBaseTestCase());

        List<Feature> features = getFeaturesRecursively(featureFolder);
        List<DriverConfig> webdrivers = BTCHelper.getDriverConfigurations(logger, configurationId);
        List<Object> instantiatedTestClasses = new ArrayList<>();

        for (Feature feature: features) {
            List<ScenarioDefinition> scenarios = feature.getChildren();
            Map<String, ScenarioDefinition> scenariosByTestMethod = new HashMap<>();

            if (scenarios.isEmpty()) {
                logger.warn("Skipping feature " + feature.getName() + ", it has no valid scenarios");
                continue;
            }

            CtClass baseBDDClass = getBDDBaseClass(feature, bddResultPackageName, baseTestCase);
            AttributeInfo attr = getAttributeInfoForTestAnnotation(baseBDDClass);
            CtMethod bdd = getBDDBaseMethod(baseBDDClass);

            for (ScenarioDefinition scenario : scenarios) {
                String testMethodName = scenario.getName().replace(" ", "_").toLowerCase();

                // Scenario is an ScenarioOutline, and as such we want to create a scenario per row of data in .feature file
                if (scenario instanceof ScenarioOutline) {
                    List<ScenarioDefinition> outlineGeneratedScenarios = getScenariosFromOutline((ScenarioOutline) scenario);

                    int scenNr = 1;
                    for (ScenarioDefinition generatedScenario: outlineGeneratedScenarios) {
                        testMethodName += "-" + scenNr; // append number to scenario to differ it
                        addMethod(bdd, baseBDDClass, testMethodName, attr, scenariosByTestMethod, generatedScenario);

                        testMethodName = testMethodName.replace("-" + scenNr, "");
                        scenNr += 1;
                    }
                }
                else {
                    addMethod(bdd, baseBDDClass, testMethodName, attr, scenariosByTestMethod, scenario);
                }

                try {
                    Class cl = baseBDDClass.toClass();
                    Constructor<?> cons = cl.getConstructor(String.class, Map.class);
                    Object newClassInstance = cons.newInstance(webdrivers.get(0).id, scenariosByTestMethod);
                    instantiatedTestClasses.add(newClassInstance);
                } catch (Exception e) {
                    String[] fixes = {};
                    Errors.logError(logger, "Failed to compile class from feature file", fixes);
                }
            }

        }

        return instantiatedTestClasses.toArray();
    }


    /**
     * Needed when running inside intellij, not to be used in production
     *
     * @return
     */
    protected String getDefaultFolderContainingFeatureFiles() {
        return "";
    }

    protected String getDefaultBaseTestCase() {
        return "BDDBaseTestCase";
    }

    private void addMethod(CtMethod bdd, CtClass baseBDDClass, String testMethodName, AttributeInfo attr, Map<String, ScenarioDefinition> scenariosByTestMethod, ScenarioDefinition scenario) {
        try {
            CtMethod methodToAdd = CtNewMethod.copy(bdd, baseBDDClass, null);
            methodToAdd.setName(testMethodName);
            methodToAdd.insertBefore("logger.debug(\"Successfully generated test during runtime\");");
            methodToAdd.getMethodInfo().addAttribute(attr);
            baseBDDClass.addMethod(methodToAdd);
            scenariosByTestMethod.put(testMethodName, scenario);
        } catch (Exception e) {
            logger.error("Could not create object, scenario: " + testMethodName);
        }
    }

    private List<ScenarioDefinition> getScenariosFromOutline(ScenarioOutline outline) {
        List<ScenarioDefinition> generatedScenarios = new ArrayList<>();

        // Populate a Map of the example values
        for (Examples example: outline.getExamples()) {
            Map<String, List<String>> exampleValues = new HashMap<>();
            List<TableCell> cells = example.getTableHeader().getCells();
            for (int i = 0; i < cells.size(); i++) {
                String header = cells.get(i).getValue();
                List<String> values = new ArrayList<>();
                for (TableRow row: example.getTableBody()) {
                    values.add(row.getCells().get(i).getValue());
                }
                exampleValues.put(header, values);
            }

            List<Step> steps = new ArrayList<>();
            for (Step step: outline.getSteps()) {
                String stepText = step.getText();
                // If we find placeholder, replace it with data
                if (Pattern.matches("(.*)<(.*?)>(.*?)", stepText)) {
                    for (Map.Entry<String, List<String>> table: exampleValues.entrySet()) {
                        String placeholder = String.format("<%s>", table.getKey());
                        for (String value: table.getValue()) {
                            if (stepText.contains(placeholder)) {
                                stepText = stepText.replace(placeholder, value);
                                steps.add(new Step(step.getLocation(), "Scenario", stepText, step.getArgument()));
                            }
                        }
                    }
                    // Add the step as is, if no placeholder is found
                } else steps.add(new Step(step.getLocation(), "Scenario", stepText, step.getArgument()));
                ScenarioDefinition generatedScenario = new Scenario(outline.getTags(), outline.getLocation(), "Scenario", outline.getName(), outline.getDescription(), steps);
                generatedScenarios.add(generatedScenario);
            }
        }
        return generatedScenarios;
    }

    @Override
    protected String getDriverConfigId() {
        return "chromedriver";
    }

    @Override
    protected String getConfigFile() {
        return "resources/config.xml";
    }

    @Override
    protected void initPages(AutomationDriver driver) {

    }
}
