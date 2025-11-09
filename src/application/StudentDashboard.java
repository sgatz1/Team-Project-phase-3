package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.util.Map;
import java.util.List;

/**
 * student dash
 * combines TP2  and email + q&a portal  with tp3 requirements:
 *  - Trusted reviewers
 *  - Request reviewer role
 *  - Send and view private messages
 */
public class StudentDashboard {

    private Stage stage;        // main window
    private DatabaseHelper db;  // database helper
    private String user;        // logged-in student name

    // reviewers
    private TrustedReviewerList trustedList = new TrustedReviewerList();
    private static final ReviewerApproval reviewerApproval = ReviewerApproval.getInstance();
    private static final MessageManager messageManager = MessageManager.getInstance();

    // constructor
    public StudentDashboard(Stage s, DatabaseHelper d, String u) {
        stage = s;
        db = d;
        user = u;
    }

    // show build
    public void show(Stage unused) {
        // === TP2 SECTION: Email + Q&A ===
        Label lblTitle = new Label("Student Dashboard");
        Label lblEmail = new Label("Current email: " + safe(db.getUserEmail(user)));

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("New email");

        Button btnUpdate = new Button("Update Email");
        Button btnBack = new Button("Back");
        Button btnQnA = new Button("Open Q&A Portal");

        Label lblStatus = new Label();

        // update email btn
        btnUpdate.setOnAction(e -> {
            String newEmail = txtEmail.getText();
            if (newEmail == null || newEmail.trim().isEmpty()) {
                lblStatus.setText("Enter a valid email.");
                return;
            }

            boolean ok = db.updateUserEmail(user, newEmail.trim());
            if (ok) {
                lblStatus.setText("Email updated.");
                lblEmail.setText("Current email: " + safe(db.getUserEmail(user)));
                txtEmail.clear();
            } else {
                lblStatus.setText("Update failed.");
            }
        });

        // q&a portal btn
        btnQnA.setOnAction(e -> {
            QuestionPage qPage = new QuestionPage(db, user);
            qPage.openPage();
        });

        // bck btn
        btnBack.setOnAction(e -> {
            WelcomeLoginPage w = new WelcomeLoginPage(db);
            w.show(stage, user);
        });

        // tp3 features we added
        Button btnTrusted = new Button("Trusted Reviewers");
        btnTrusted.setOnAction(e -> openTrustedWindow());

        Button btnRequest = new Button("Request Reviewer Role");
        btnRequest.setOnAction(e -> openRequestWindow());

        Button btnMessages = new Button("Send Message");
        btnMessages.setOnAction(e -> openMessageWindow());

        Button btnInbox = new Button("View Inbox");
        btnInbox.setOnAction(e -> openInboxWindow());

        // gui layout
        VBox root = new VBox(10,
                lblTitle,
                lblEmail,
                txtEmail,
                btnUpdate,
                lblStatus,
                btnQnA,
                new Separator(),
                new Label("TP3 Features:"),
                btnTrusted,
                btnRequest,
                btnMessages,
                btnInbox,
                btnBack
        );

        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        //  setup of dashboard for student
        stage.setScene(new Scene(root, 800, 550));
        stage.setTitle("Student Dashboard");
    }

    //reviewer interface
    private void openTrustedWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Trusted Reviewers List");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // sample trusted reviewers
        trustedList.addTrustedReviewer("ReviewerJohn", 10);
        trustedList.addTrustedReviewer("ReviewerLisa", 8);
        trustedList.addTrustedReviewer("ReviewerMark", 6);

        // TableView setup
        TableView<Map.Entry<String, Integer>> table = new TableView<>();

        TableColumn<Map.Entry<String, Integer>, String> nameCol = new TableColumn<>("Reviewer");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        nameCol.setPrefWidth(150);

        TableColumn<Map.Entry<String, Integer>, String> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getValue())));
        weightCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, weightCol);
        table.getItems().addAll(trustedList.getTrustedMap().entrySet());

        box.getChildren().addAll(title, table);

        popup.setScene(new Scene(box, 300, 300));
        popup.setTitle("Trusted Reviewers");
        popup.show();
    }

    // === TP3: Request Reviewer Role ===
    private void openRequestWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Request to Become a Reviewer");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea txtReason = new TextArea();
        txtReason.setPromptText("Why would you like to become a reviewer?");
        txtReason.setWrapText(true);
        txtReason.setPrefSize(300, 100);

        Button btnSend = new Button("Send Request");
        Label lblStatus = new Label();

        btnSend.setOnAction(e -> {
            String reason = txtReason.getText().trim();
            if (reason.isEmpty()) {
                lblStatus.setText("Please enter a reason before sending.");
                return;
            }
// request sent logic
            ReviewerRequest req = new ReviewerRequest(user, reason);
            reviewerApproval.addRequest(req);

            lblStatus.setText("Your request was sent!");
            txtReason.clear();
        });

        box.getChildren().addAll(title, txtReason, btnSend, lblStatus);

        popup.setScene(new Scene(box, 400, 250));
        popup.setTitle("Request Reviewer Role");
        popup.show();
    }

    // messaging popup
    private void openMessageWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label lblTitle = new Label("Send a Message");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> cmbUsers = new ComboBox<>();
        List<String> allUsers = db.getAllUsernames(); // from DatabaseHelper
        cmbUsers.getItems().addAll(allUsers);
        cmbUsers.setPromptText("Select a user to message");

        TextField txtMsgTitle = new TextField();
        txtMsgTitle.setPromptText("Message Title");

        TextArea txtBody = new TextArea();
        txtBody.setPromptText("Write your message here...");
        txtBody.setWrapText(true);
        txtBody.setPrefSize(300, 120);

        Button btnSend = new Button("Send Message");
        Label lblStatus = new Label();

        btnSend.setOnAction(e -> {
            String receiver = cmbUsers.getValue();
            String title = txtMsgTitle.getText().trim();
            String body = txtBody.getText().trim();

            if (receiver == null || title.isEmpty() || body.isEmpty()) {
                lblStatus.setText(" Please fill out all fields!.");
                return;
            }

            messageManager.sendMessage(user, receiver, title, body);
            lblStatus.setText("Message sent!");
            txtMsgTitle.clear();
            txtBody.clear();
        });

        box.getChildren().addAll(lblTitle, cmbUsers, txtMsgTitle, txtBody, btnSend, lblStatus);

        popup.setScene(new Scene(box, 400, 400));
        popup.setTitle("Messages");
        popup.show();
    }

    //inbox interface //typically a popup
    private void openInboxWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label lblTitle = new Label("📬 Inbox for " + user);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<Message> messages = messageManager.getMessagesForUser(user);

        if (messages.isEmpty()) {
            box.getChildren().addAll(lblTitle, new Label("No messages yet."));
        } else {
            for (Message msg : messages) {
                Label sender = new Label("From: " + msg.getSender());
                Label title = new Label("Title: " + msg.getTitle());
                Label body = new Label("Message: " + msg.getBody());
                Separator sep = new Separator();

                VBox msgBox = new VBox(5, sender, title, body, sep);
                msgBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
                box.getChildren().add(msgBox);
            }
            box.getChildren().add(0, lblTitle);
        }

        popup.setScene(new Scene(box, 400, 400));
        popup.setTitle("Inbox");
        popup.show();
    }

    // helper to avoid nulls
    private String safe(String s) {
        return (s == null ? "(none)" : s);
    }
}
