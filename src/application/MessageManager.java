package application;

import java.util.ArrayList;
import java.util.List;

 // Manages sending and receiving private messages between users.
public class MessageManager {

    // single shared instance
    private static final MessageManager instance = new MessageManager();

    // shared list of all messages
    private final List<Message> messageList = new ArrayList<>();

    // private constructor to prevent new instances
    private MessageManager() {}

    // access point for the shared instance
    public static MessageManager getInstance() {
        return instance;
    }

    // sends a message
    public void sendMessage(String sender, String receiver, String title, String body) {
        Message msg = new Message(sender, receiver, title, body);
        messageList.add(msg);
        System.out.println("[MSG] " + sender + " -> " + receiver + " | " + title);
    }

    // gets all messages sent to a particular user
    public List<Message> getMessagesForUser(String username) {
        List<Message> result = new ArrayList<>();
        for (Message m : messageList) {
            if (m.getReceiver().equalsIgnoreCase(username)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Message> getAllMessages() {
        return messageList;
    }

    public void clearAll() {
        messageList.clear();
    }
}
