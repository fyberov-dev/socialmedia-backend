package ee.taltech.iti0302project.exceptions.chat;

public class ChatNoAccessException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    public ChatNoAccessException(String message) {
        super(message);
    }
}
