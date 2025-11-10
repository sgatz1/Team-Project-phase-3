package tests;

import databasePart1.DatabaseHelper;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseHelperTest {

    private static DatabaseHelper db;

    @BeforeAll
    static void setup() {
        db = new DatabaseHelper();
    }

    @Test
    void testRegisterAndLogin() {
        String username = "test_user_" + System.currentTimeMillis();
        String password = "test_pass";
        String email = username + "@mail.com";

        assertTrue(db.register(username, password, email), "User should be registered successfully");
        assertTrue(db.login(username, password), "User should be able to log in");
    }

    @Test
    void testAddQuestionAndRetrieve() {
        String username = "q_user_" + System.currentTimeMillis();
        db.register(username, "123", username + "@mail.com");

        boolean added = db.addQuestion(username, "Test Title", "Test Body");
        assertTrue(added, "Question should be added");

        List<String[]> questions = db.getQuestionsByUser(username);
        assertFalse(questions.isEmpty(), "Should return at least one question");
        assertEquals("Test Title", questions.get(0)[2]);
    }

    @Test
    void testAddReviewAndRetrieve() {
        String reviewer = "rev_" + System.currentTimeMillis();
        db.register(reviewer, "revpass", reviewer + "@mail.com");
        db.addRole(reviewer, "Reviewer");

        db.addQuestion(reviewer, "Q Title", "Q Body");
        int qid = Integer.parseInt(db.getAllQuestions().get(0)[0]);

        boolean added = db.addReview(qid, reviewer, "Looks good", "Highly Reliable");
        assertTrue(added, "Review should be added");

        var reviews = db.getReviewsForQuestion(qid);
        assertFalse(reviews.isEmpty(), "There should be at least one review");
        assertEquals("Highly Reliable", reviews.get(0)[2]);
    }

    @Test
    void testReviewerFiltering() {
        var reviewedQuestions = db.getQuestionsReviewedByReviewers();
        assertNotNull(reviewedQuestions, "Reviewed questions list should not be null");
    }
}
