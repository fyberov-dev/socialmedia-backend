package ee.taltech.iti0302project.response;

import ee.taltech.iti0302project.dto.user.UserProfileDto;
import lombok.Getter;

public class UserProfileResponse {
    private final UserProfileDto dto;
    @Getter
    private final String message;

    public UserProfileResponse(UserProfileDto dto, String message) {
        this.dto = dto;
        this.message = message;
    }

    public UserProfileDto getDTO() {
        return dto;
    }

}
