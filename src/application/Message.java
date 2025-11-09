package application;

// data architecture for the messaging system i built
public class Message {

    private String sender;
    private String receiver;
    private String title;
    private String body;

    public Message(String sender, String receiver, String title, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getTitle() { return title; }
    public String getBody() { return body; }

    @Override
    public String toString() {
        return "From: " + sender + " | To: " + receiver +
                " | Title: " + title + " | Body: " + body;
    }
}
