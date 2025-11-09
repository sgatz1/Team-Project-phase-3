package application;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of trusted reviewers and their trust value (weight)
 */
public class TrustedReviewerList {

    // stores each reviewer name and their weight value
    private Map<String, Integer> trustedMap;

    // creates the map when this class is made
    public TrustedReviewerList() {
        trustedMap = new HashMap<>();
    }

    // add a reviewer and give them a weight
    public void addTrustedReviewer(String reviewerName, int weight) {
        trustedMap.put(reviewerName, weight);
    }

    // remove a reviewer from the list
    public void removeTrustedReviewer(String reviewerName) {
        trustedMap.remove(reviewerName);
    }

    // get the weight value for a reviewer (0 if not found)
    public int getReviewerWeight(String reviewerName) {
        return trustedMap.getOrDefault(reviewerName, 0);
    }

    // update the weight for a reviewer if they already exist
    public void updateWeight(String reviewerName, int newWeight) {
        if (trustedMap.containsKey(reviewerName)) {
            trustedMap.put(reviewerName, newWeight);
        }
    }

    // print out all reviewers and their weights
    public void printTrustedReviewers() {
        for (String name : trustedMap.keySet()) {
            System.out.println(name + " -> Weight: " + trustedMap.get(name));
        }
    }

    // clear all reviewers from the list
    public void clearAll() {
        trustedMap.clear();
    }

    // return the full map of trusted reviewers
    public Map<String, Integer> getTrustedMap() {
        return trustedMap;
    }
}
