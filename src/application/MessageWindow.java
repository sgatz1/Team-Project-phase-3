package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

// this displays a window of all messages recieved from other users/students or staff even
public class MessageWindow {

    private MessageManager messageManager;
    private DatabaseHelper db;
    private String currentUser;

    public MessageWindow(MessageManager manager, DatabaseHelper db, String user) {
        this.messageManager = manager;
        this.db = db;
        this.currentUser = user;
    }

    public void show(Stage parent) {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-alignment: center;");

        Label titleLabel = new Label("Send a Message");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // dropdown of all usernames from DB
        ComboBox<String> cmbUsers = new ComboBox<>();
        List<String> allUsers = db.getAllUsernames(); // ensure your DatabaseHelper has this
        cmbUsers.getItems().addAll(allUsers);
        cmbUsers.setPromptText("Select a user to message");

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Message Title");

        TextArea txtBody = new TextArea();
        txtBody.setPromptText("Type your message here...");
        txtBody.setWrapText(true);
        txtBody.setPrefSize(300, 100);

        Button btnSend = new Button("Send Message");
        Label lblStatus = new Label();

        btnSend.setOnAction(e -> {
            String receiver = cmbUsers.getValue();
            String title = txtTitle.getText().trim();
            String body = txtBody.getText().trim();

            if (receiver == null || title.isEmpty() || body.isEmpty()) {
                lblStatus.setText("⚠️ Please fill out all fields.");
                return;
            }

            messageManager.sendMessage(currentUser, receiver, title, body);
            lblStatus.setText("Message sent!");
            txtTitle.clear();
            txtBody.clear();
        });

        box.getChildren().addAll(titleLabel, cmbUsers, txtTitle, txtBody, btnSend, lblStatus);

        Scene scene = new Scene(box, 400, 350);
        popup.setScene(scene);
        popup.setTitle("Messages");
        popup.initOwner(parent);
        popup.show();
    }
}
