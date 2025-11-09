package application;

/**
 * This class represents a Review in the system.
 * Each review belongs to a reviewer and is linked to either a question or an answer.
 */
public class Review {

    private String reviewerName;   // the person who wrote the review
    private String content;        // what the reviewer wrote
    private String targetItem;     // what this review is about (a question or an answer)
    private int rating;            // simple rating from incorrect, reliable, unaccepted standards

    // constructor to make a new review
    public Review(String reviewerName, String content, String targetItem, int rating) {
        this.reviewerName = reviewerName;
        this.content = content;
        this.targetItem = targetItem;
        this.rating = rating;
    }

    // get and set methods below

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(String targetItem) {
        this.targetItem = targetItem;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // shows the review as one line of text
    @Override
    public String toString() {
        return "Review by " + reviewerName + " on " + targetItem +
               " (Rating: " + rating + ") - " + content;
    }
}
