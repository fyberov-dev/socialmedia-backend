package ee.taltech.iti0302project.dto.auth;

import lombok.Data;

@Data
public class AuthResponseDto {

    private final String accessToken;

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
