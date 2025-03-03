package ee.taltech.iti0302project.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login into profile")
public class LoginDto {

    @Schema(description = "Username", example = "John")
    private String username;

    @Schema(description = "Password", example = "P$ssw0Rd12345")
    private String password;
}
