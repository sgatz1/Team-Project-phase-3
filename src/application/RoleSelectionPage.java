package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// This page lets the user pick their role (Admin, Student, Reviewer, or Staff)
// After they pick, it updates their role in the database and sends them to the right page
public class RoleSelectionPage {

    private final DatabaseHelper db;
    private final String username;

    // gets the database helper and current username
    public RoleSelectionPage(DatabaseHelper d, String user) {
        this.db = d;
        this.username = user;
    }

    // shows the role selection screen
    public void show(Stage stage) {
        // main title and small prompt text
        Label lblTitle = new Label("Select Your Role");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label lblPrompt = new Label("Choose your role for this account:");

        // dropdown with available roles
        ComboBox<String> cmbRoles = new ComboBox<>();
        cmbRoles.getItems().addAll("Admin", "Student", "Reviewer", "Staff");
        cmbRoles.setPromptText("Select a Role");

        // confirm button and label to show status
        Button btnConfirm = new Button("Confirm");
        Label lblStatus = new Label();

        // when confirm is clicked
        btnConfirm.setOnAction(e -> {
            String selectedRole = cmbRoles.getValue();
            if (selectedRole == null) {
                lblStatus.setText("Please select a role before continuing.");
                return;
            }

            // update role in the database
            boolean ok = db.updateUserRole(username, selectedRole);
            if (ok) {
                lblStatus.setText("Role assigned: " + selectedRole);

                // open the correct home page for the selected role
                switch (selectedRole.toLowerCase()) {
                    case "admin":
                        AdminHomePage aPage = new AdminHomePage(db);
                        aPage.show(stage);
                        break;

                    case "student":
                        StudentDashboard dash = new StudentDashboard(stage, db, username);
                        dash.show(stage);
                        break;

                    case "reviewer":
                        ReviewerHomePage rPage = new ReviewerHomePage(stage, db, username);
                        rPage.show(stage);
                        break;

                    case "staff":
                        StaffHomePage sPage2 = new StaffHomePage(stage, db, username);
                        sPage2.show(stage);
                        break;

                    default:
                        lblStatus.setText("Unknown role selected.");
                }
            } else {
                lblStatus.setText("Database update failed.");
            }
        });

        // layout setup
        VBox root = new VBox(15, lblTitle, lblPrompt, cmbRoles, btnConfirm, lblStatus);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // make the window
        Scene scene = new Scene(root, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Role Selection");
        stage.show();
    }
}
