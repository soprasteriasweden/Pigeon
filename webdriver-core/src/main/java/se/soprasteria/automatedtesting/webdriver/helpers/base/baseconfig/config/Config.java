package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement
public class Config {

    @XmlElement(name = "property")
    @XmlElementWrapper(name = "properties")
    public List<Property> properties;

    @XmlElement(name="email")
    @XmlElementWrapper(name = "emails")
    public List<Email> emails;

    @XmlElement(name="user")
    @XmlElementWrapper(name = "users")
    public List<User> users;

    @XmlElement(name="driver", required=true)
    @XmlElementWrapper(name = "drivers")
    public List<DriverConfig> webdriverConfigurations;

    public String getProperty(String name) {
        if (properties != null) {
            for (Property property : properties) {
                if (property.name.contentEquals(name)) {
                    return property.value;
                }
            }
        }
        return null;
    }

}
