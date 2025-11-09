package application;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of trusted reviewers and their weight values.
 * A higher weight means the reviewer is more trusted or helpful.
 */
public class TrustedReviewerList {

    // stores reviewer name -> weight value
    private Map<String, Integer> trustedMap;

    // constructor initializes the map
    public TrustedReviewerList() {
        trustedMap = new HashMap<>();
    }



      @param reviewerName the name of the reviewer
      @param weight

     // a category of weightage. the higher the weight the more reliable the reviewer is and trusted they r.
    public void addTrustedReviewer(String reviewerName, int weight) {
        trustedMap.put(reviewerName, weight);
    }

     // eradicates  a reviewer from the trusted list.
     @param reviewerName

    public void removeTrustedReviewer(String reviewerName) {
        trustedMap.remove(reviewerName);
    }

    // retrieves the trust weight

    // name of the reviewer themselves
      @param reviewerName
      @return

     // assigns the weight of 0 if there is no weight to be dedicated to the reviewer
    public int getReviewerWeight(String reviewerName) {
        return trustedMap.getOrDefault(reviewerName, 0);
    }

      @param reviewerName the name of the reviewer
      @param newWeight the new weight value

     // the new weight value
     // the name or identifier of the reviewer
     // update of the value for the specific reviewer
    public void updateWeight(String reviewerName, int newWeight) {
        if (trustedMap.containsKey(reviewerName)) {
            trustedMap.put(reviewerName, newWeight);
        }
    }

   // prints the demo trusted reviewers and their weightage
    public void printTrustedReviewers() {
        for (String name : trustedMap.keySet()) {
            System.out.println(name + " -> Weight: " + trustedMap.get(name));
        }
    }

   // clears and re-initates the entire reviewer list if needed
    public void clearAll() {
        trustedMap.clear();
    }
// returns the map of all the reviewers thus far into the application, i left this as a demo - aziz
     @return
    public Map<String, Integer> getTrustedMap() {
        return trustedMap;
    }
}
