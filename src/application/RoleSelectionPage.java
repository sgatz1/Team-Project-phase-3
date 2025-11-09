package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

 //The RoleSelectionPage class shows a simple dropdown menu
// that lets a user pick their role (Admin, Student, Reviewer, or Staff).
 // upon selection, it updates the role in the database and redirects our users
public class RoleSelectionPage {

    private final DatabaseHelper db;
    private final String username;


     @param d // the db helper  used for database actions
     @param user // user name of the logged in user display
    public RoleSelectionPage(DatabaseHelper d, String user) {
        this.db = d;
        this.username = user;
    }

     // role selection screen for the user to handle re-direction to the  application
    @param stage the main application window

    public void show(Stage stage) {
        // title and labeling setup
        Label lblTitle = new Label("Select Your Role");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblPrompt = new Label("Choose your role for this account:");

        // box with all available roles to select based on intended use
        ComboBox<String> cmbRoles = new ComboBox<>();
        cmbRoles.getItems().addAll("Admin", "Student", "Reviewer", "Staff");
        cmbRoles.setPromptText("Select a Role");

        // confirm  button and status label
        Button btnConfirm = new Button("Confirm");
        Label lblStatus = new Label();

        // action for confrim button
        btnConfirm.setOnAction(e -> {
            String selectedRole = cmbRoles.getValue();
            if (selectedRole == null) {
                lblStatus.setText("Please select a role before continuing.");
                return;
            }

            boolean ok = db.updateUserRole(username, selectedRole);
            if (ok) {
                lblStatus.setText("Role assigned: " + selectedRole);

                // redirects users to correct page based on chosen role
                switch (selectedRole.toLowerCase()) {
                    case "admin":
                        AdminHomePage aPage = new AdminHomePage(db);
                        aPage.show(stage);
                        break;

                    case "student":
                        // NEW: Show our new TP3 Student page
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

        // layout and styling
        VBox root = new VBox(15, lblTitle, lblPrompt, cmbRoles, btnConfirm, lblStatus);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // creating gui backdrop
        Scene scene = new Scene(root, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Role Selection");
        stage.show();
    }
}
