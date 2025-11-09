package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

// reviewer interface and home page when they login
public class ReviewerHomePage {

    private Stage stage;
    private DatabaseHelper db;
    private String user;

    // shared message manager
    private static final MessageManager messageManager = MessageManager.getInstance();
    public ReviewerHomePage(Stage s, DatabaseHelper d, String u) {
        this.stage = s;
        this.db = d;
        this.user = u;
    }

    // main reviewer dashbboard
    public void show(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        Label lblTitle = new Label("Reviewer Dashboard");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblWelcome = new Label("Welcome, " + user + "!");

        //q&a portal
        Button btnQnA = new Button("Open Q&A Forum");
        btnQnA.setOnAction(e -> {
            QuestionPage qPage = new QuestionPage(db, user);
            qPage.openPage();
        });

        // viewing msg
        Button btnViewMessages = new Button("View Messages");
        btnViewMessages.setOnAction(e -> openMessagesWindow());

        // sending msg
        Button btnSendMessage = new Button("Send Message");
        btnSendMessage.setOnAction(e -> openSendMessageWindow());

        //logout
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new SetupLoginSelectionPage(db).show(stage));

        root.getChildren().addAll(
                lblTitle,
                lblWelcome,
                new Separator(),
                btnQnA,
                btnViewMessages,
                btnSendMessage,
                new Separator(),
                btnLogout
        );

        stage.setScene(new Scene(root, 450, 400));
        stage.setTitle("Reviewer Home");
        stage.show();
    }

   // opens a listing
    private void openMessagesWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");
        Label lbl = new Label("Messages for " + user);
        lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<Message> list = messageManager.getMessagesForUser(user);
        if (list.isEmpty()) {
            box.getChildren().addAll(lbl, new Label("No messages yet."));
        } else {
            for (Message m : list) {
                Label from = new Label("From: " + m.getSender());
                Label title = new Label("Title: " + m.getTitle());
                Label body = new Label("Body: " + m.getBody());
                VBox msgBox = new VBox(4, from, title, body, new Separator());
                msgBox.setStyle("-fx-border-color: lightgray; -fx-padding: 8;");
                box.getChildren().add(msgBox);
            }
        }

        popup.setScene(new Scene(box, 400, 400));
        popup.setTitle("Inbox — " + user);
        popup.show();
    }

    // opens up a popup allowing the reviewer to send a msg to any user also
    private void openSendMessageWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");

        Label lbl = new Label("Send a Message");
        lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> cmbUsers = new ComboBox<>();
        for (String[] u : db.getAllUsers()) {
            cmbUsers.getItems().add(u[0]);
        }
        cmbUsers.setPromptText("Select a recipient");

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Enter title");

        TextArea txtBody = new TextArea();
        txtBody.setPromptText("Enter message body");

        Button btnSend = new Button("Send");
        Label lblStatus = new Label();

        btnSend.setOnAction(e -> {
            String receiver = cmbUsers.getValue();
            String title = txtTitle.getText().trim();
            String body = txtBody.getText().trim();

            if (receiver == null || title.isEmpty() || body.isEmpty()) {
                lblStatus.setText("Please fill in all fields.");
                return;
            }

            messageManager.sendMessage(user, receiver, title, body);
            lblStatus.setText("Message sent to " + receiver + "!");
            txtTitle.clear();
            txtBody.clear();
            cmbUsers.setValue(null);
        });

        box.getChildren().addAll(lbl, cmbUsers, txtTitle, txtBody, btnSend, lblStatus);
        popup.setScene(new Scene(box, 400, 400));
        popup.setTitle("Send Message");
        popup.show();
    }
}
