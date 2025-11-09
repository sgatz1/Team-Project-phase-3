package application;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all reviews in the system.
 * It can add, edit, delete, and list reviews.
 */
public class ReviewManager {

    private List<Review> reviewList;  // list that holds all reviews

    // constructor makes an empty list of reviews
    public ReviewManager() {
        reviewList = new ArrayList<>();
    }

    // adds a new review to the list
    public void addReview(Review review) {
        reviewList.add(review);
    }

    // edits an existing review's content and rating
    public void editReview(int index, String newContent, int newRating) {
        if (index >= 0 && index < reviewList.size()) {
            Review r = reviewList.get(index);
            r.setContent(newContent);
            r.setRating(newRating);
        }
    }

    // deletes a review from the list
    public void deleteReview(int index) {
        if (index >= 0 && index < reviewList.size()) {
            reviewList.remove(index);
        }
    }

    // returns all reviews
    public List<Review> getAllReviews() {
        return reviewList;
    }

    // prints all reviews for testing
    public void printAllReviews() {
        for (Review r : reviewList) {
            System.out.println(r);
        }
    }
}
