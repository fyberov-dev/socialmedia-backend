package ee.taltech.iti0302project.dto;

import ee.taltech.iti0302project.dto.user.UserDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private UserDto author;
    private Date createdAt; // Creation timestamp
}
