package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/**
 * The StudentHomePage class represents the home page for a student user.
 * It includes options to view trusted reviewers and request to become a reviewer.
 */
public class StudentHomePage {

    private Stage stage;         // main window
    private DatabaseHelper db;   // db actions
    private String user;         // logged in student name

    // reviewer logic
    private TrustedReviewerList trustedList = new TrustedReviewerList();
    private ReviewerApproval reviewerApproval = ReviewerApproval.getInstance();

   // student home page function
    public StudentHomePage(Stage s, DatabaseHelper d, String u) {
        this.stage = s;
        this.db = d;
        this.user = u;
    }

// student home interface
    public void show(Stage stage) {
        Label label = new Label("Student Home Page for " + user);

        // btn to view/manage trusted reviewers
        Button trustedBtn = new Button("Trusted Reviewers");
        trustedBtn.setOnAction(e -> openTrustedWindow());

        // btn to request to become a reviewer
        Button requestBtn = new Button("Request Reviewer Role");
        requestBtn.setOnAction(e -> openRequestWindow());

        // laying out of the box
        VBox root = new VBox(10, label, trustedBtn, requestBtn);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");

        stage.setScene(new Scene(root, 400, 250));
        stage.setTitle("Student Home");
        stage.show();
    }

     // opens a demo pop up of the reviewers that are trusted & their respective weights
    private void openTrustedWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");
        Label title = new Label("Trusted Reviewers Test");

        // Example: add one trusted reviewer and print the list
        trustedList.addTrustedReviewer("ReviewerJohn", 10);
        trustedList.printTrustedReviewers();

        Label info = new Label("Added a trusted reviewer! Check console for output.");
        box.getChildren().addAll(title, info);
        popup.setScene(new Scene(box, 300, 150));
        popup.setTitle("Trusted Reviewers");
        popup.show();
    }

   // pop up to prompt user to enter reason for wanting to become a reviewer
   // this gets sent to staff/admin for accepting or denying of said request
    private void openRequestWindow() {
        Stage popup = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-alignment: center; -fx-padding: 20;");
        Label title = new Label("Reviewer Request Test");


        // student sends out a request line
        ReviewerRequest req = new ReviewerRequest(user, "I want to help improve answers!");
        reviewerApproval.addRequest(req);
        reviewerApproval.printAllRequests();

        Label info = new Label("Request created! Check console for output.");
        box.getChildren().addAll(title, info);
        popup.setScene(new Scene(box, 350, 150));
        popup.setTitle("Request Reviewer Role");
        popup.show();
    }
}
