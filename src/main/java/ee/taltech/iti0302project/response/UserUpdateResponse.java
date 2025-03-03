package ee.taltech.iti0302project.response;

import ee.taltech.iti0302project.dto.user.UserUpdateDTO;
import lombok.Getter;


@Getter
public class UserUpdateResponse {

    private final UserUpdateDTO user;
    private final String message;

    public UserUpdateResponse(UserUpdateDTO user, String message) {
        this.user = user;
        this.message = message;
    }
}
