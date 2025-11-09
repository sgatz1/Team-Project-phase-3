package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

 // form  that allows reviewers to write and submit a review for a specific question.
 // when they  submit, the review is saved and the corresponding question is marked as reviewed.

public class ReviewForm {
    private final DatabaseHelper db;
    private final int qid;
    private final String reviewer;

    public ReviewForm(DatabaseHelper db, int qid, String reviewer) {
        this.db = db;
        this.qid = qid;
        this.reviewer = reviewer;
    }

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label lbl = new Label("Write a review for Question #" + qid);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        TextArea txtBody = new TextArea();
        txtBody.setPromptText("Enter your review text...");

        ComboBox<String> cmbReliability = new ComboBox<>();
        cmbReliability.getItems().addAll("Highly Reliable", "Unclear", "Incorrect");
        cmbReliability.setPromptText("Select Reliability Rating");

        Button btnSubmit = new Button("Submit Review");
        Label msg = new Label();

        btnSubmit.setOnAction(e -> {
            String body = txtBody.getText().trim();
            String rel = cmbReliability.getValue();

            if (body.isEmpty() || rel == null) {
                msg.setText(" Please enter your review text and select reliability.");
                return;
            }

            // stores the review (assumes addReview exists in DatabaseHelper)
            db.addReview(qid, reviewer, body, rel);

            //  marks q&a questions as reviewed so it appears

            // this later allows us to filter 'show reviewed only' questions
            db.markQuestionReviewed(qid);

            msg.setText(" Review submitted! Question marked as reviewed.");
            txtBody.clear();
            cmbReliability.getSelectionModel().clearSelection();
        });

        root.getChildren().addAll(lbl, txtBody, cmbReliability, btnSubmit, msg);
        stage.setScene(new Scene(root, 420, 320));
        stage.setTitle("Submit Review — " + reviewer);
        stage.show();
    }
}
