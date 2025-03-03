package ee.taltech.iti0302project.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Register new user")
public class RegisterDto {

    @Schema(description = "Username", example = "John")
    @Size(min = 4, max = 50, message = "User's name should be from 4 to 50 characters long")
    private String username;

    @Schema(description = "Email", example = "user@example.com")
    @Size(min = 5, max = 255, message = "User's email should be from 5 to 255 characters long")
    @Email(message = "Email should be in format user@example.com")
    private String email;

    @Schema(description = "Password", example = "P$ssw0Rd12345")
    @Size(min = 8, max = 255, message = "Password should be from 8 to 255 characters long")
    private String password;

    @Schema(description = "User's name", example = "John")
    @Size(min = 4, max = 50, message = "User's name should be from 4 to 50 characters long")
    private String name;

    @Schema(description = "User's surname", example = "Smith")
    @Size(min = 4, max = 50, message = "User's surname should be from 4 to 50 characters long")
    private String surname;
}
