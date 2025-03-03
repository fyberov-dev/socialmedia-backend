package ee.taltech.iti0302project.exceptions.post;

public class PostEditNoAccessException extends RuntimeException {

    private static final long serialVersionUID = 6L;

    public PostEditNoAccessException(String message) {
        super(message);
    }
}
