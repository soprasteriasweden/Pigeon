package se.soprasteria.automatedtesting.webdriver.api.utility;

import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.User;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.credentials.UserHelper;

import java.util.List;


/**
 *
 * Manages credentials for a user and provides an interface for either creating users yourself or extracting them from
 * the XML. Please refer to the users.xml file in the resource folder of the reference implementation to see how to
 * provide users to the system.
 *
 */
public class Credentials {

    private final User user;
    private final List<User> userList;

    /**
     * Initialize a user with dummy values (id:dummy, user:badUsername, pw:badPassword, pin:9876) and provide an
     * interface to the XML file with users.
     */
    public Credentials() {
        userList = BaseTestConfig.getInstance().getConfig().users;
        user = UserHelper.getDummyUser();
        userList.add(user);
    }

    /**
     * Initialize a user based on userid and XML file.
     *
     * @param UserID UserID that matches with an id found in the credentials XML file.
     */
    public Credentials(String UserID) {
        userList = BaseTestConfig.getInstance().getConfig().users;
        user =  UserHelper.getUserById(userList, UserID);
    }

    /**
     * Add a user into the current pool of users. This user will only be added to the runtime users, not the user XML.
     *
     * @param id
     * @param role
     * @param username
     * @param pw
     * @param pin
     */
    public void addUser(String id, String role, String username, String pw, String pin) {
        userList.add(UserHelper.getUser(id, role, username, pw, pin));
    }

    /**
     * Get the ID of the user that was specified in the constructor. If nothing was specified, return dummy ID.
     *
     * @return String
     */
    public String getId() {
        return UserHelper.getId(user);
    }

    /**
     * Get the role of the user that was specified in the constructor. If nothing was specified, return dummy role.
     *
     * @return String
     */
    public String getRole() {
        return UserHelper.getRole(user);
    }

    /**
     * Get the role of the user based on userid.
     *
     * @param userId
     * @return
     */
    public String getRole(String userId) {
        return UserHelper.getRole(UserHelper.getUserById(userList, userId));
    }

    /**
     * Get the username of the user that was specified in the constructor. If nothing was specified, return dummy
     * username.
     *
     * @return String
     */
    public String getUsername() {
        return UserHelper.getUsername(user);
    }

    /**
     * Get the username of this user based on userId.
     *
     * @param userId
     * @return username of user with matching userID.
     */
    public String getUsername(String userId) {
        return UserHelper.getUsername(UserHelper.getUserById(userList, userId));
    }

    /**
     * Get the password of the user that was specified in the constructor. If nothing was specified, return dummy pw.
     *
     * @return String cleartext password.
     */
    public String getPassword() {
        return UserHelper.getDecodedPassword(user);
    }

    /**
     * Get the password of the user based on userid.
     *
     * @param userId
     * @return password of user with matching userID.
     */
    public String getPassword(String userId) {
        return UserHelper.getDecodedPassword(UserHelper.getUserById(userList, userId));
    }

    /**
     * Get the pin of the user that was specified in the constructor. If nothing was specified, return dummy pin.
     *
     * @return String cleartext pin.
     */
    public String getPin() {
        return UserHelper.getDecodedPin(user);
    }

    /**
     * Get the pin for this user based on userid.
     *
     * @param userId
     * @return pin of the user with the matching userID.
     */
    public String getPin(String userId) {
        return UserHelper.getDecodedPin(UserHelper.getUserById(userList, userId));
    }

}
