package application;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The {@code ReviewerApproval} class manages a centralized list of
 * reviewer role requests and handles their approval or rejection.
 * </p>
 *
 * <p>
 * This class uses the Singleton design pattern, ensuring that only one
 * instance of {@code ReviewerApproval} exists during program execution.
 * Requests are stored in an internal list, and each can be approved or
 * rejected by index.
 * </p>
 */
public class ReviewerApproval {

    /** The single instance of this class (Singleton pattern). */
    private static ReviewerApproval instance;

    /**
     * Returns the single shared instance of {@code ReviewerApproval}.
     * If it does not exist yet, it will be created.
     *
     * @return the single {@code ReviewerApproval} instance
     */
    public static ReviewerApproval getInstance() {
        if (instance == null) {
            instance = new ReviewerApproval();
        }
        return instance;
    }

    /** The list that stores all reviewer requests. */
    private final List<ReviewerRequest> requests = new ArrayList<>();

    /**
     * Private constructor to enforce the Singleton pattern.
     * Prevents direct instantiation outside this class.
     */
    private ReviewerApproval() { }

    /**
     * Adds a new reviewer request to the internal list.
     *
     * @param req the {@link ReviewerRequest} to be added
     */
    public void addRequest(ReviewerRequest req) {
        requests.add(req);
    }

    /**
     * Approves a reviewer request at the specified index.
     * If the index is invalid, no action is taken.
     *
     * @param index the index of the request to approve
     */
    public void approveRequest(int index) {
        if (index >= 0 && index < requests.size()) {
            requests.get(index).setApproved(true);
        }
    }

    /**
     * Rejects (removes) a reviewer request at the specified index.
     * If the index is invalid, no action is taken.
     *
     * @param index the index of the request to reject
     */
    public void rejectRequest(int index) {
        if (index >= 0 && index < requests.size()) {
            requests.remove(index);
        }
    }

    /**
     * Prints all reviewer requests and their details to the console.
     * Intended primarily for debugging and testing.
     */
    public void printAllRequests() {
        for (ReviewerRequest r : requests) {
            System.out.println(r);
        }
    }

    /**
     * Returns the list of all reviewer requests.
     * This can be used by GUI components to display the current requests.
     *
     * @return a {@link java.util.List} of {@link ReviewerRequest} objects
     */
    public List<ReviewerRequest> getRequests() {
        return requests;
    }

    /**
     * Removes all reviewer requests from the list.
     * Useful for resetting or reinitializing the system.
     */
    public void clearAll() {
        requests.clear();
    }
}
