package application;

import java.util.ArrayList;
import java.util.List;

public class ReviewerApproval {

    // dependent setup
    private static ReviewerApproval instance;
    public static ReviewerApproval getInstance() {
        if (instance == null) {
            instance = new ReviewerApproval();
        }
        return instance;
    }

    // data
    private final List<ReviewerRequest> requests = new ArrayList<>();

    private ReviewerApproval() { }   // private so only instances create it

    // Add a new request
    public void addRequest(ReviewerRequest req) {
        requests.add(req);
    }

    // Approve request by index
    public void approveRequest(int index) {
        if (index >= 0 && index < requests.size()) {
            requests.get(index).setApproved(true);
        }
    }

    // Reject/remove request by index
    public void rejectRequest(int index) {
        if (index >= 0 && index < requests.size()) {
            requests.remove(index);
        }
    }

    // Print all requests (debug use)
    public void printAllRequests() {
        for (ReviewerRequest r : requests) {
            System.out.println(r);
        }
    }

    // Return list (for GUI)
    public List<ReviewerRequest> getRequests() {
        return requests;
    }

    // Optional helper to clear list
    public void clearAll() {
        requests.clear();
    }
}
