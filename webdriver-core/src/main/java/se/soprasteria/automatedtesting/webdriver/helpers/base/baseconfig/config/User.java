package se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *  A datastructure used for a single user. The format in the annotation caters the specification of the
 *  datafiles containing user credentials.
 *
 */
public class User {
    @XmlAttribute(name="id", required=true)
    public String id;
    @XmlAttribute(name="role", required=true)
    public String role;
    @XmlAttribute(name="username", required=true)
    public String username;
    @XmlAttribute(name="password", required=true)
    public String password;
    @XmlAttribute(name="pin", required=false)
    public String pin;
}
