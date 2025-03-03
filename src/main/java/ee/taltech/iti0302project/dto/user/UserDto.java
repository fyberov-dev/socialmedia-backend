package ee.taltech.iti0302project.dto.user;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
}
