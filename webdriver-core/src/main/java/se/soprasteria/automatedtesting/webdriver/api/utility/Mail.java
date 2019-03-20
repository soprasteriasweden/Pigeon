package se.soprasteria.automatedtesting.webdriver.api.utility;

import se.soprasteria.automatedtesting.webdriver.helpers.utility.mail.MailClient;

import javax.mail.Store;

/**
 * A simple mailclient to use when performing tests.
 */
public class Mail {
    private Store store;

    /**
     * Connect to the mail specified in the propertiesfile.
     *
     * @param user email account username.
     * @param pw email account password.
     * @param propertiesPath path to a mailproperties file, see reference implementation for format.
     */
    public Mail(String user, String pw, String propertiesPath) {
        store = MailClient.getInstance().connect(user, pw, propertiesPath);
    }


}
