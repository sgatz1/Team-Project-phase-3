package application;

/**
 * <p>
 * The {@code Message} class represents a single message exchanged between users
 * in the application's messaging system. Each message contains a sender, a receiver,
 * a title, and a body.
 * </p>
 */
public class Message {

    /** The username of the person sending the message. */
    private String sender;

    /** The username of the person receiving the message. */
    private String receiver;

    /** The title or subject of the message. */
    private String title;

    /** The main content or body of the message. */
    private String body;

    /**
     * Constructs a new {@code Message} with the given details.
     *
     * @param sender   the username of the sender
     * @param receiver the username of the receiver
     * @param title    the title or subject of the message
     * @param body     the body text of the message
     */
    public Message(String sender, String receiver, String title, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    /**
     * Returns the username of the message sender.
     *
     * @return the sender’s username
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the username of the message receiver.
     *
     * @return the receiver’s username
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns the title or subject of the message.
     *
     * @return the message title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the text body of the message.
     *
     * @return the message body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns a formatted string representation of the message,
     * showing sender, receiver, title, and body.
     *
     * @return a readable string containing message details
     */
    @Override
    public String toString() {
        return "From: " + sender + " | To: " + receiver +
                " | Title: " + title + " | Body: " + body;
    }
}
