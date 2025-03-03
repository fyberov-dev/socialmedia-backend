package ee.taltech.iti0302project.exceptions.user;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
