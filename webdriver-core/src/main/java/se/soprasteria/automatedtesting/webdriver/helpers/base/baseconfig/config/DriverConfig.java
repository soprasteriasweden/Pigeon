package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config;

import se.soprasteria.automatedtesting.webdriver.api.utility.SystemManagement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 *
 * A datastructure used for a single configuration, only change if the XML format changes.
 *
 */

@SuppressWarnings("unused")
public class DriverConfig {
    @XmlAttribute(name = "id", required = true)
    public String id;
    @XmlAttribute(name = "runtimeEnvironment")
    public String runtimeEnvironment;
    @XmlAttribute(name="implicit", required=false)
    public int implicit;
    @XmlAttribute(name="pageLoad", required=false)
    public int pageLoad;
    @XmlAttribute(name = "type", required = true)
    public String type;
    @XmlAttribute(name = "url", required = false)
    public String url;
    @XmlAttribute(name = "version", required = false)
    public String version;


    @XmlElement(name = "capability")
    public List<Capability> capabilities;
    @XmlElement(name = "option")
    public List<Option> options;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Option {
        @XmlAttribute(name="name", required = true)
        public String name;
        @XmlAttribute(name="value", required = true)
        public String value;
        @XmlAttribute(name="type", required = true)
        public String type;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Capability {
        @XmlAttribute(name="type", required=false)
        public String type;
        @XmlAttribute(name="alternateValue", required=false)
        public String alternateValue;
        @XmlAttribute(name="name", required=true)
        public String name;
        @XmlAttribute(name="value", required=true)
        private String value;

        public String getValue() {
            if (type != null) {
                if (type.contentEquals("resource")) {
                    return SystemManagement.resourceGetAbsolutePath(this.value);
                }
            }
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
