package se.soprasteria.automatedtesting.webdriver.helpers.utility.session;

import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.DriverConfig.Capability;

import java.util.ArrayList;
import java.util.List;

public class SessionCapabilities {

    private static List<Capability> sessionCapabilities = new ArrayList<>();


    public static void setCapability(Capability capability) {
        sessionCapabilities.add(capability);
    }

    public static String getCapability(String name) {
        String value = "";

        for(Capability capability : sessionCapabilities) {
            if(capability.name.equals(name)) value = capability.getValue();
        }

        if(value.equals("")) throw new IllegalArgumentException("Could not find the capability " + name);
        return value;
    }
}
