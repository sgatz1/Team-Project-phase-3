package application;

/**
 * <p>
 * The {@code Answer} class represents an individual answer in the Q&A system.
 * Each answer contains text, the author's name, and an indicator of whether it
 * has been accepted.
 * </p>
 */
public class Answer {

    /** The text content of the answer. */
    private String text;

    /** The username or identifier of the author who wrote the answer. */
    private String author;

    /** Whether this answer has been marked as accepted. */
    private boolean accepted;

    /**
     * Constructs a new {@code Answer} with the given text and author.
     * By default, the answer is not accepted.
     *
     * @param text   the text content of the answer
     * @param author the name of the user who authored the answer
     */
    public Answer(String text, String author) {
        this.text = text;
        this.author = author;
        this.accepted = false;
    }

    /**
     * Returns the text content of the answer.
     *
     * @return the text of the answer
     */
    public String getText() {
        return text;
    }

    /**
     * Updates the text content of the answer.
     *
     * @param text the new text for the answer
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the author of the answer.
     *
     * @return the author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Checks whether the answer has been accepted.
     *
     * @return {@code true} if the answer is accepted; {@code false} otherwise
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets whether the answer has been accepted.
     *
     * @param accepted {@code true} to mark the answer as accepted; {@code false} otherwise
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Returns a string representation of the answer, including the author and text.
     *
     * @return a formatted string describing the answer
     */
    @Override
    public String toString() {
        return "Answer by " + author + ": " + text;
    }
}
