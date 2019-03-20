package se.soprasteria.automatedtesting.webdriver.helpers.bdd;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ParserException;
import gherkin.TokenMatcher;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;


public class BDDHelper {

    public static Feature getFeature(File featureFile) {
        String in1 = null;
        try {
            in1 = FileUtils.readFileToString(featureFile, Charset.defaultCharset());
        } catch (Exception e) {
            return null;
        }

        TokenMatcher matcher = new TokenMatcher();
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        GherkinDocument doc = null;
        try {
            doc = parser.parse(in1, matcher);
        } catch (ParserException e) {
            return null;

        }
        return doc.getFeature();
    }

}
