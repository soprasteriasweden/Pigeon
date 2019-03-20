package se.soprasteria.automatedtesting.webdriver.helpers.utility.credentials;

import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.User;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.errors.Console;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.Variables;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Base64;
import java.util.List;


public class UserHelper {

    public static User parseXML(String xmlPath) {
        Variables.throwExceptionOnNull(xmlPath, "Need to provide a path for the XML");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            File file = new File(xmlPath);
            return (User) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            String[] fixes = {"Verify that XML exists", "Verify that XML is valid"};
            String message = "Failed to parse XML containing user credentials. Check XML at " + xmlPath;
            throw new RuntimeException(Console.getErrorMessage(message, fixes));
        }
    }

    public static User getDummyUser() {
        return getUser("dummy", "dummy", "badUsername", "badPassword", "9876");
    }

    public static User getUser(String id, String role, String username, String password, String pin) {
        User user = new User();
        user.id = id;
        user.role = role;
        user.username = username;
        setAndEncodePassword(user, password);
        setAndEncodePin(user, pin);
        return user;
    }

    public static String getDecodedPassword(User user) {
        return new String(Base64.getDecoder().decode(user.password));
    }

    public static void setAndEncodePassword(User user, String password) {
        user.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String getDecodedPin(User user) {
        return new String(Base64.getDecoder().decode(user.pin));
    }

    public static void setAndEncodePin(User user, String pin) {
        user.pin = Base64.getEncoder().encodeToString(pin.getBytes());
    }

    public static User getUserById(List<User> users, String id) {
        for (User user: users) {
            if (user.id.contentEquals(id)) {
                return user;
            }
        }
        throw new RuntimeException("Probable bug, tried to find a user with an invalid ID");
    }

    public static String getRole(User user) {
        return user.role;
    }

    public static String getId(User user) {
        return user.id;
    }

    public static String getUsername(User user) {
        return user.username;
    }
}
