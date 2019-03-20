package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config;

import javax.xml.bind.annotation.XmlAttribute;


public class Property {
    @XmlAttribute(name="name", required = true)
    public String name;
    @XmlAttribute(name="value", required = true)
    public String value;
}
