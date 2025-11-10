package application;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The {@code ReviewManager} class is responsible for managing all reviews
 * within the system. It supports adding, editing, deleting, and retrieving
 * reviews, as well as printing them for testing or debugging purposes.
 * </p>
 *
 * <p>
 * Internally, all reviews are stored in a {@link java.util.List} of
 * {@link Review} objects.
 * </p>
 */
public class ReviewManager {

    /** A list that holds all reviews in the system. */
    private List<Review> reviewList;

    /**
     * Constructs a new {@code ReviewManager} with an empty list of reviews.
     */
    public ReviewManager() {
        reviewList = new ArrayList<>();
    }

    /**
     * Adds a new review to the system.
     *
     * @param review the {@link Review} to be added
     */
    public void addReview(Review review) {
        reviewList.add(review);
    }

    /**
     * Edits the content and rating of an existing review.
     * If the index is invalid, no action is taken.
     *
     * @param index       the index of the review to edit
     * @param newContent  the updated content for the review
     * @param newRating   the updated rating value
     */
    public void editReview(int index, String newContent, int newRating) {
        if (index >= 0 && index < reviewList.size()) {
            Review r = reviewList.get(index);
            r.setContent(newContent);
            r.setRating(newRating);
        }
    }

    /**
     * Deletes a review at the specified index.
     * If the index is invalid, no action is taken.
     *
     * @param index the index of the review to delete
     */
    public void deleteReview(int index) {
        if (index >= 0 && index < reviewList.size()) {
            reviewList.remove(index);
        }
    }

    /**
     * Returns the complete list of all reviews.
     *
     * @return a {@link java.util.List} containing all {@link Review} objects
     */
    public List<Review> getAllReviews() {
        return reviewList;
    }

    /**
     * Prints all reviews to the console.
     * Intended for testing or debugging purposes only.
     */
    public void printAllReviews() {
        for (Review r : reviewList) {
            System.out.println(r);
        }
    }
}
