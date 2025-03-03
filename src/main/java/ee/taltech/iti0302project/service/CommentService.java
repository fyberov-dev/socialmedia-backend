package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.CommentDto;
import ee.taltech.iti0302project.entity.CommentEntity;
import ee.taltech.iti0302project.entity.PostEntity;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.post.PostNotFoundException;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import ee.taltech.iti0302project.mapper.CommentMapper;
import ee.taltech.iti0302project.repository.CommentRepository;
import ee.taltech.iti0302project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public CommentDto addComment(Long postId, String content) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        UserEntity user = userService.getCurrentUser();

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        return commentMapper.toDtoList(commentRepository.findByPostId(postId));
    }
}
