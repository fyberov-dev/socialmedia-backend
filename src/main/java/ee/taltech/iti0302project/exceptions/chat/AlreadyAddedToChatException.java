package ee.taltech.iti0302project.exceptions.chat;

public class AlreadyAddedToChatException extends RuntimeException {

    private static final long serialVersionUID = 3L;

    public AlreadyAddedToChatException(String message) {
        super(message);
    }
}
