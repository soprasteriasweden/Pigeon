package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config;

import javax.xml.bind.annotation.XmlAttribute;


public class Email {
    @XmlAttribute(name="type", required = true)
    public String type;
    @XmlAttribute(name="host", required = true)
    public String host;
    @XmlAttribute(name="port", required = true)
    public String port;
    @XmlAttribute(name="socketFactoryPort", required = true)
    public String socketFactoryPort;
    @XmlAttribute(name="socketFactory", required = true)
    public String socketFactory;
    @XmlAttribute(name="auth", required = true)
    public String auth;
}
