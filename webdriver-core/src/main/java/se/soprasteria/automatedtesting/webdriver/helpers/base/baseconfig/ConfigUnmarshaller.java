package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig;

import se.soprasteria.automatedtesting.webdriver.api.utility.SystemManagement;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.Config;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;


public class ConfigUnmarshaller {

    public static Config getData(String pathToConfigFile) {
        pathToConfigFile = SystemManagement.resourceGetAbsolutePath(pathToConfigFile);
        String pathToValidationSchema = SystemManagement.moveResourceToTemporaryDirectory("config.xsd", "config.xsd");

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = factory.newSchema(new StreamSource(new File(pathToValidationSchema)));
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(schema);
            File file = new File(pathToConfigFile);
            return (Config) jaxbUnmarshaller.unmarshal(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}