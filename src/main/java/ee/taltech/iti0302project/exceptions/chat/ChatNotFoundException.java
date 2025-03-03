package ee.taltech.iti0302project.exceptions.chat;

public class ChatNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ChatNotFoundException(String message) {
        super(message);
    }
}
