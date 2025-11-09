package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * staff home page — provides ability to access to q&A, messaging, logout,
 * as well as the  ability to approve reviewer requests from students.
 */
public class StaffHomePage {

    private final Stage stage;
    private final DatabaseHelper db;
    private final String user;

    // message manager
    private static final MessageManager messageManager = MessageManager.getInstance();

    // reviwer apporval for staff and admin
    private ReviewerApproval reviewerApproval = ReviewerApproval.getInstance();
    public StaffHomePage(Stage stage, DatabaseHelper db, String user) {
        this.stage = stage;
        this.db = db;
        this.user = user;
    }

    public void show(Stage stage) {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label title = new Label("Staff Home Page — Welcome, " + user);
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // btns on interface
        Button qnaBtn = new Button("Open Q&A Portal");
        Button viewRequestsBtn = new Button("View Reviewer Requests");
        Button sendMsgBtn = new Button("Send Message");
        Button viewMsgsBtn = new Button("View Messages");
        Button logoutBtn = new Button("Logout");

        Label status = new Label();

        // q&A Portal
        qnaBtn.setOnAction(e -> {
            QuestionPage qPage = new QuestionPage(db, user);
            qPage.openPage();
        });

        // viewing of reviewer Requests
        viewRequestsBtn.setOnAction(e -> showReviewerRequests());

        // sending messages
        sendMsgBtn.setOnAction(e -> openMessageSender());

        // view messages
        viewMsgsBtn.setOnAction(e -> openInbox());

        // logout logic
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(db).show(stage);
        });

        root.getChildren().addAll(
                title,
                qnaBtn,
                viewRequestsBtn,
                sendMsgBtn,
                viewMsgsBtn,
                logoutBtn,
                status
        );

        Scene scene = new Scene(root, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Staff Home");
        stage.show();
    }

    // prompt for reviewer request
    private void showReviewerRequests() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        Label header = new Label("Pending Reviewer Requests");
        header.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        List<ReviewerRequest> requests = reviewerApproval.getRequests();
        if (requests.isEmpty()) {
            box.getChildren().add(new Label("No reviewer requests at this time."));
        } else {
            for (ReviewerRequest req : requests) {
                VBox card = new VBox(5);
                card.setStyle("-fx-border-color: gray; -fx-border-radius: 4; -fx-padding: 8;");
                Label from = new Label("From: " + req.getUsername());
                Label reason = new Label("Reason: " + req.getReason());
                HBox actions = new HBox(10);
                Button approve = new Button("Approve");
                Button reject = new Button("Reject");

                approve.setOnAction(ev -> {
                    db.updateUserRole(req.getUsername(), "reviewer");
                    reviewerApproval.approveRequest(requests.indexOf(req));
                    box.getChildren().remove(card);
                    showAlert("Approved", "Reviewer role granted to " + req.getUsername());
                });

                reject.setOnAction(ev -> {
                    reviewerApproval.rejectRequest(requests.indexOf(req));
                    box.getChildren().remove(card);
                    showAlert("Rejected", "Request rejected for " + req.getUsername());
                });

                actions.getChildren().addAll(approve, reject);
                card.getChildren().addAll(from, reason, actions);
                box.getChildren().add(card);
            }
        }

        Scene scene = new Scene(box, 420, 350);
        popup.setTitle("Reviewer Requests");
        popup.setScene(scene);
        popup.show();
    }

    // opens prompt box for ability to send any user a msg
    // this is used as a shortcut to allow students to directly send a msg to the reviewers also
    private void openMessageSender() {
        Stage popup = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label header = new Label("Send Message");
        ComboBox<String> cmbUsers = new ComboBox<>();
        for (String[] u : db.getAllUsers()) {
            cmbUsers.getItems().add(u[0]); // username
        }
        cmbUsers.setPromptText("Select user");

        TextField title = new TextField();
        title.setPromptText("Message Title");

        TextArea body = new TextArea();
        body.setPromptText("Enter message body");

        Button send = new Button("Send");
        Label status = new Label();

        send.setOnAction(e -> {
            String receiver = cmbUsers.getValue();
            if (receiver == null || receiver.isEmpty()) {
                status.setText("Please select a user.");
                return;
            }
            messageManager.sendMessage(user, receiver, title.getText(), body.getText());
            status.setText("Message sent to " + receiver + "!");
            title.clear();
            body.clear();
        });

        root.getChildren().addAll(header, cmbUsers, title, body, send, status);
        popup.setScene(new Scene(root, 400, 350));
        popup.setTitle("Send Message");
        popup.show();
    }

   // inbox messages for the users
    private void openInbox() {
        Stage popup = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label header = new Label("Inbox for " + user);
        header.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        List<Message> inbox = messageManager.getMessagesForUser(user);
        if (inbox.isEmpty()) {
            root.getChildren().add(new Label("No messages."));
        } else {
            for (Message msg : inbox) {
                VBox card = new VBox(4);
                card.setStyle("-fx-border-color: gray; -fx-padding: 6;");
                card.getChildren().addAll(
                        new Label("From: " + msg.getSender()),
                        new Label("Title: " + msg.getTitle()),
                        new Label("Body: " + msg.getBody())
                );
                root.getChildren().add(card);
            }
        }

        popup.setScene(new Scene(root, 420, 350));
        popup.setTitle("Messages");
        popup.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
