package application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * Admin page where admins can:
 * - see list of users
 * - add/remove roles
 * - delete users
 * - generate invitation codes
 * - open Q&A forum
 * - view/send messages
 * - review reviewer requests
 * - view trusted reviewers
 */
public class AdminHomePage {

    private DatabaseHelper db;
    private TableView<String[]> table;
    private ObservableList<String[]> rows;

    // reviweers for TP3 features
    private static final MessageManager messageManager = MessageManager.getInstance();
    private ReviewerApproval reviewerApproval = ReviewerApproval.getInstance();
    private TrustedReviewerList trustedList = new TrustedReviewerList();

    public AdminHomePage(DatabaseHelper db) {
        this.db = db;
    }

    public void show(Stage stage) {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));

        // headers
        HBox header = new HBox(8);
        Label lbl = new Label("Admin Panel");
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new SetupLoginSelectionPage(db).show(stage));
        header.getChildren().addAll(lbl, btnLogout);

        // user table creation
        table = new TableView<>();
        table.setPrefHeight(260);
        TableColumn<String[], String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[0]));
        TableColumn<String[], String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[1]));
        TableColumn<String[], String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[2]));
        TableColumn<String[], String> colRoles = new TableColumn<>("Roles");
        colRoles.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[3]));
        table.getColumns().addAll(colUser, colName, colEmail, colRoles);

        Button btnRefresh = new Button("Refresh Users");
        btnRefresh.setOnAction(e -> loadUsers());

        // role authority
        TextField txtUser = new TextField();
        txtUser.setPromptText("username");
        TextField txtRole = new TextField();
        txtRole.setPromptText("role (admin/student/staff/reviewer)");
        Button btnAddRole = new Button("Add Role");
        Button btnRemoveRole = new Button("Remove Role");
        Label roleMsg = new Label();

        btnAddRole.setOnAction(e -> {
            String uname = txtUser.getText().trim();
            String role = txtRole.getText().trim();
            if (uname.isEmpty() || role.isEmpty()) {
                roleMsg.setText("Enter username and role");
                return;
            }
            boolean ok = addRole(uname, role);
            roleMsg.setText(ok ? "Role added." : "Could not add role.");
            if (ok) loadUsers();
        });

        btnRemoveRole.setOnAction(e -> {
            String uname = txtUser.getText().trim();
            String role = txtRole.getText().trim();
            if (uname.isEmpty() || role.isEmpty()) {
                roleMsg.setText("Enter username and role");
                return;
            }
            if (role.equalsIgnoreCase("admin") && isAdmin(uname) && countAdmins() <= 1) {
                roleMsg.setText("At least one admin must remain.");
                return;
            }
            boolean ok = removeRole(uname, role);
            roleMsg.setText(ok ? "Role removed." : "Could not remove role.");
            if (ok) loadUsers();
        });

        HBox roleBox = new HBox(8, txtUser, txtRole, btnAddRole, btnRemoveRole);

        // --- delete user ---
        TextField txtDelUser = new TextField();
        txtDelUser.setPromptText("username");
        TextField txtConfirm = new TextField();
        txtConfirm.setPromptText("Type YES");
        Button btnDelete = new Button("Delete");
        Label delMsg = new Label();

        btnDelete.setOnAction(e -> {
            String uname = txtDelUser.getText().trim();
            String conf = txtConfirm.getText().trim();
            if (uname.isEmpty()) {
                delMsg.setText("Enter a username.");
                return;
            }
            if (!"YES".equals(conf)) {
                delMsg.setText("Type YES to confirm.");
                return;
            }
            if (isAdmin(uname) && countAdmins() <= 1) {
                delMsg.setText("Cannot delete last admin.");
                return;
            }
            boolean ok = deleteUser(uname);
            delMsg.setText(ok ? "User deleted." : "Delete failed.");
            if (ok) loadUsers();
        });

        HBox delBox = new HBox(8, txtDelUser, txtConfirm, btnDelete);

        // generation of codes for new users
        Button btnCode = new Button("Generate Code");
        Label codeMsg = new Label();
        btnCode.setOnAction(e -> {
            String code = db.generateInvitationCode();
            codeMsg.setText(code != null ? "New code: " + code : "Could not generate code");
        });

        // q&a portal
        Button btnQnA = new Button("Open Q&A Portal");
        btnQnA.setStyle("-fx-font-weight: bold; -fx-background-color: #0078D7; -fx-text-fill: white;");
        btnQnA.setOnAction(e -> {
            QuestionPage qPage = new QuestionPage(db, "AdminUser");
            qPage.openPage();
        });

        // requirements we needed for tp3
        Button btnMessages = new Button("View All Messages");
        btnMessages.setOnAction(e -> openMessagesWindow());

        Button btnSendMsg = new Button("Send Message");
        btnSendMsg.setOnAction(e -> openSendMessageWindow());

        Button btnReviewers = new Button("Reviewer Requests");
        btnReviewers.setOnAction(e -> openReviewerRequests());

        Button btnTrusted = new Button("View Trusted Reviewers");
        btnTrusted.setOnAction(e -> openTrustedReviewers());

        // layout
        VBox cardUsers = new VBox(8, new Label("Users:"), table, btnRefresh);
        VBox cardRoles = new VBox(6, new Label("Change role:"), roleBox, roleMsg);
        VBox cardDelete = new VBox(6, new Label("Delete user (YES to confirm):"), delBox, delMsg);
        VBox cardInvites = new VBox(6, new Label("Invitation codes:"), btnCode, codeMsg);
        VBox cardQnA = new VBox(6, new Label("Q&A Portal:"), btnQnA);
        VBox cardTP3 = new VBox(8,
                new Label("TP3 Features:"),
                btnMessages,
                btnSendMsg,
                btnReviewers,
                btnTrusted
        );

        root.getChildren().addAll(header, cardUsers, cardRoles, cardDelete, cardInvites, cardQnA, cardTP3);

        loadUsers();
        stage.setScene(new Scene(root, 880, 820));
        stage.setTitle("Admin");
    }

    // show all msgs
    private void openMessagesWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");
        Label lbl = new Label("All Sent Messages");
        lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<Message> all = messageManager.getAllMessages();
        if (all.isEmpty()) {
            box.getChildren().addAll(lbl, new Label("No messages found."));
        } else {
            for (Message m : all) {
                Label from = new Label("From: " + m.getSender());
                Label to = new Label("To: " + m.getReceiver());
                Label title = new Label("Title: " + m.getTitle());
                Label body = new Label("Body: " + m.getBody());
                VBox msgBox = new VBox(4, from, to, title, body, new Separator());
                msgBox.setStyle("-fx-border-color: lightgray; -fx-padding: 8;");
                box.getChildren().add(msgBox);
            }
        }
        popup.setScene(new Scene(box, 450, 500));
        popup.setTitle("All Messages");
        popup.show();
    }

    // send a msg as admin
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
            messageManager.sendMessage("Admin", receiver, title, body);
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

    // requests coming in to be reviewer viewer
    private void openReviewerRequests() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");
        Label lbl = new Label("Reviewer Requests");
        lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<ReviewerRequest> requests = reviewerApproval.getRequests();
        if (requests.isEmpty()) {
            box.getChildren().addAll(lbl, new Label("No reviewer requests yet."));
        } else {
            for (int i = 0; i < requests.size(); i++) {
                ReviewerRequest r = requests.get(i);
                Label name = new Label("From: " + r.getUsername());
                Label reason = new Label("Reason: " + r.getReason());
                Label status = new Label("Approved: " + r.isApproved());
                Button approve = new Button("Approve");
                Button reject = new Button("Reject");
                int index = i;
                approve.setOnAction(e -> {
                    reviewerApproval.approveRequest(index);
                    status.setText("Approved: true");
                });
                reject.setOnAction(e -> {
                    reviewerApproval.rejectRequest(index);
                    box.getChildren().remove(name.getParent());
                });
                VBox reqBox = new VBox(4, name, reason, status, new HBox(6, approve, reject), new Separator());
                reqBox.setStyle("-fx-border-color: lightgray; -fx-padding: 8;");
                box.getChildren().add(reqBox);
            }
        }
        popup.setScene(new Scene(box, 450, 500));
        popup.setTitle("Reviewer Requests");
        popup.show();
    }

    // list of current trusted reviewers, this is a demo at the moment
    private void openTrustedReviewers() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");
        Label lbl = new Label("Trusted Reviewers List");
        lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        trustedList.addTrustedReviewer("ReviewerJohn", 10);
        trustedList.addTrustedReviewer("ReviewerLisa", 8);
        trustedList.addTrustedReviewer("ReviewerMark", 6);

        for (Map.Entry<String, Integer> entry : trustedList.getTrustedMap().entrySet()) {
            box.getChildren().add(new Label(entry.getKey() + " — Weight: " + entry.getValue()));
        }

        popup.setScene(new Scene(box, 300, 300));
        popup.setTitle("Trusted Reviewers");
        popup.show();
    }

    // admin backend logic
    private void loadUsers() {
        try {
            List<String[]> list = db.getAllUsers();
            rows = FXCollections.observableArrayList(list);
            table.setItems(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addRole(String u, String r) {
        try {
            db.addRole(u, r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean removeRole(String u, String r) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:file:C:/Users/aibrahim/cse360db", "sa", "Password");
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ROLES WHERE USERNAME = ? AND ROLE = ?")) {
            ps.setString(1, u);
            ps.setString(2, r);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean deleteUser(String u) {
        db.deleteUser(u);
        return true;
    }

    private boolean isAdmin(String u) {
        List<String> roles = db.getUserRoles(u);
        return roles.contains("admin");
    }

    private int countAdmins() {
        int count = 0;
        List<String[]> users = db.getAllUsers();
        for (String[] u : users) {
            if (u[3].toLowerCase().contains("admin")) count++;
        }
        return count;
    }
}
