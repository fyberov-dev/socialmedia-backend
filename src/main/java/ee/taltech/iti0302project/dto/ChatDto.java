package ee.taltech.iti0302project.dto;

import ee.taltech.iti0302project.dto.user.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatDto {

    private Long id;
    private String name;
    private UserDto creator;
    private List<UserDto> members;
}
