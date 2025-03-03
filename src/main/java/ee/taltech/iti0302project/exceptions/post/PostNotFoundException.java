package ee.taltech.iti0302project.exceptions.post;

public class PostNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5L;

    public PostNotFoundException(String message) {
        super(message);
    }
}
