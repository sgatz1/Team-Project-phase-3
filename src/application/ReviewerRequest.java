package application;

/**
 * Simple class to represent a reviewer role request.
 */
public class ReviewerRequest {

    private String username; // person making the request
    private String reason;   // reason they want to be a reviewer
    private boolean approved;

    public ReviewerRequest(String username, String reason) {
        this.username = username;
        this.reason = reason;
        this.approved = false;
    }

    // --- Getters and Setters ---
    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "ReviewerRequest{" +
                "username='" + username + '\'' +
                ", reason='" + reason + '\'' +
                ", approved=" + approved +
                '}';
    }
}
