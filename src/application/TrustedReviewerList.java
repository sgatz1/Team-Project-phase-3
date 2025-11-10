package application;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The {@code TrustedReviewerList} class manages a collection of trusted reviewers
 * and their corresponding trust weights. This can be used to track reviewer
 * reliability, reputation, or ranking within a system.
 * </p>
 *
 * <p>
 * Internally, it stores reviewer names and their trust values using a
 * {@link java.util.HashMap}. Each reviewer is associated with an integer
 * weight that can be retrieved, updated, or removed.
 * </p>
 */
public class TrustedReviewerList {

    /** 
     * Stores each reviewer name and their corresponding trust weight.
     */
    private Map<String, Integer> trustedMap;

    /**
     * Constructs a new {@code TrustedReviewerList} and initializes
     * the internal map used to track trusted reviewers.
     */
    public TrustedReviewerList() {
        trustedMap = new HashMap<>();
    }

    /**
     * Adds a new trusted reviewer with the specified weight.
     *
     * @param reviewerName the name of the reviewer
     * @param weight       the trust weight assigned to the reviewer
     */
    public void addTrustedReviewer(String reviewerName, int weight) {
        trustedMap.put(reviewerName, weight);
    }

    /**
     * Removes a reviewer from the trusted list.
     *
     * @param reviewerName the name of the reviewer to remove
     */
    public void removeTrustedReviewer(String reviewerName) {
        trustedMap.remove(reviewerName);
    }

    /**
     * Retrieves the trust weight for the specified reviewer.
     *
     * @param reviewerName the name of the reviewer
     * @return the trust weight if the reviewer exists, or {@code 0} if not found
     */
    public int getReviewerWeight(String reviewerName) {
        return trustedMap.getOrDefault(reviewerName, 0);
    }

    /**
     * Updates the trust weight for an existing reviewer.
     * If the reviewer does not exist, no action is taken.
     *
     * @param reviewerName the name of the reviewer
     * @param newWeight    the new trust weight to assign
     */
    public void updateWeight(String reviewerName, int newWeight) {
        if (trustedMap.containsKey(reviewerName)) {
            trustedMap.put(reviewerName, newWeight);
        }
    }

    /**
     * Prints all trusted reviewers and their associated weights
     * to the console output.
     */
    public void printTrustedReviewers() {
        for (String name : trustedMap.keySet()) {
            System.out.println(name + " -> Weight: " + trustedMap.get(name));
        }
    }

    /**
     * Clears all reviewers from the trusted list.
     */
    public void clearAll() {
        trustedMap.clear();
    }

    /**
     * Returns the internal map of trusted reviewers and their weights.
     *
     * @return a {@link java.util.Map} of reviewer names to weights
     */
    public Map<String, Integer> getTrustedMap() {
        return trustedMap;
    }
}
