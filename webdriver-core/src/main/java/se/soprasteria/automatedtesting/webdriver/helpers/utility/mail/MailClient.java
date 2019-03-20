package se.soprasteria.automatedtesting.webdriver.helpers.utility.mail;

import se.soprasteria.automatedtesting.webdriver.helpers.utility.errors.Console;

import javax.mail.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Easy Mail Client
 *
 * Example usage:
 *
 * Store store = MailClient.getInstance().connect("ott.autotest1@gmail.com", "b1ppb0pp100Q", "src/test/java/resources/mail.properties");
 * Message msg = MailClient.getInstance().getLastMessageFrom(store, "noreply@teliasonera.com");
 *
 * //... do anything with this message
 * if (msg != null) {
 *     String text = msg.getContent().toString();
 *     String subj = msg.getSubject();
 * }
 *
 * MailClient.getInstance().disconnect(store);
 *

 */

@SuppressWarnings("unused")
public class MailClient {
    private static final int DEFAULT_LIMIT = 10;

    private static volatile MailClient sInstance;

    public static MailClient getInstance() {
        MailClient localInstance = sInstance;
        if (localInstance == null) {
            synchronized (MailClient.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new MailClient();
                }
            }
        }
        return localInstance;
    }

    public Store connect(String mail, String password, String propertiesPath) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new File(propertiesPath)));
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(props.getProperty("mail.smtp.host"), mail, password);
            return store;
        } catch (IOException e) {
            String[] fixes = {"Ensure that the mail file exists",
                    "Ensure mail properties file is valid"};
            throw new RuntimeException(Console.getErrorMessage("Failed to open file with mail properties", fixes));
        } catch (Exception e) {
            String[] fixes = {"Ensure that the mail properties are correct",
                    "Ensure that you can connect successfully with credentials and URL using another mail client"};
            throw new RuntimeException(Console.getErrorMessage("Failed to access mail", fixes));
        }
    }

    public void disconnect(Store store) throws MessagingException {
        store.close();
    }

    public List<Message> getMessages(Store store, int limit) throws MessagingException {
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);

        Message[] allMessages = inbox.getMessages();
        int allMessagesCount = inbox.getMessageCount();

        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            if (allMessagesCount - 1 - i >= 0) {
                messageList.add(allMessages[allMessagesCount - 1 - i]);
            }
        }
        return messageList;
    }

    public List<Message> getMessages(Store store, int limit, MessageCondition condition) throws MessagingException {
        return getMessages(store, limit)
                .stream()
                .filter(message -> {
                    try {
                        return condition.test(message);
                    } catch (MessagingException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public Message getLastMessage(Store store, MessageCondition condition) throws MessagingException {
        return getMessages(store, DEFAULT_LIMIT)
                .stream()
                .filter(message -> {
                    try {
                        return condition.test(message);
                    } catch (MessagingException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    public Message getLastMessageFrom(Store store, String from, boolean containsLink) throws MessagingException {
        return getLastMessage(store, message -> {
            Address[] addresses = message.getFrom();
            try {
                return addresses.length > 0
                        && addresses[0].toString().equals(from)
                        && (!containsLink || (message.getContent() instanceof String &&
                        ((String) message.getContent()).contains("http")));
            } catch (IOException e) {
                return false;
            }
        });
    }

    public Message getLastMessageWithSubject(Store store, String subject) throws MessagingException {
        return getLastMessage(store, message -> message.getSubject().equals(subject));
    }

    public interface MessageCondition {
        boolean test(Message message) throws MessagingException;
    }
}
