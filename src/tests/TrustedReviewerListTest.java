package tests;

import application.TrustedReviewerList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrustedReviewerListTest {

    @Test
    void testAddAndRetrieveWeight() {
        TrustedReviewerList list = new TrustedReviewerList();
        list.addTrustedReviewer("alex", 5);
        assertEquals(5, list.getReviewerWeight("alex"));
    }

    @Test
    void testUpdateWeight() {
        TrustedReviewerList list = new TrustedReviewerList();
        list.addTrustedReviewer("aziz", 3);
        list.updateWeight("aziz", 7);
        assertEquals(7, list.getReviewerWeight("aziz"));
    }

    @Test
    void testRemoveReviewer() {
        TrustedReviewerList list = new TrustedReviewerList();
        list.addTrustedReviewer("bob", 2);
        list.removeTrustedReviewer("bob");
        assertEquals(0, list.getReviewerWeight("bob"));
    }

    @Test
    void testClearAll() {
        TrustedReviewerList list = new TrustedReviewerList();
        list.addTrustedReviewer("anna", 4);
        list.clearAll();
        assertTrue(list.getTrustedMap().isEmpty());
    }
}
