package application;

/**
 * <p><b>ReviewerRequest</b> represents a user's request to become a reviewer in the system.</p>
 *
 * <p>This class stores the username of the requester, their reason for applying, 
 * and the current approval status of their request. It is typically used by 
 * administrative or reviewer approval modules to process reviewer applications.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 *     ReviewerRequest req = new ReviewerRequest("alice123", "I want to review technical content.");
 *     req.setApproved(true);
 * </pre>
 *
 * @version 1.0
 */
public class ReviewerRequest {

    /** The username of the user requesting reviewer access. */
    private String username;

    /** The user's reason or justification for becoming a reviewer. */
    private String reason;

    /** Whether this request has been approved or not. */
    private boolean approved;

    /**
     * Constructs a new {@code ReviewerRequest} with the specified username and reason.
     * The request is initialized as not approved.
     *
     * @param username the username of the requester
     * @param reason the reason or explanation provided by the user
     */
    public ReviewerRequest(String username, String reason) {
        this.username = username;
        this.reason = reason;
        this.approved = false;
    }

    /**
     * Gets the username of the user who made this request.
     *
     * @return the username of the requester
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the reason the user provided for wanting to become a reviewer.
     *
     * @return the user's reason text
     */
    public String getReason() {
        return reason;
    }

    /**
     * Checks whether this request has been approved.
     *
     * @return {@code true} if the request has been approved, {@code false} otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Updates the approval status of this request.
     *
     * @param approved {@code true} to mark the request as approved, 
     *                 {@code false} to mark it as pending or rejected
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns a string representation of this reviewer request,
     * including username, reason, and approval status.
     *
     * @return a formatted string describing the request
     */
    @Override
    public String toString() {
        return "ReviewerRequest{" +
                "username='" + username + '\'' +
                ", reason='" + reason + '\'' +
                ", approved=" + approved +
                '}';
    }
}
