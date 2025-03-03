package ee.taltech.iti0302project.dto;

import ee.taltech.iti0302project.dto.user.UserDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private String content;
    private PostDto post;
    private UserDto user;
    private LocalDateTime createdAt;
}
