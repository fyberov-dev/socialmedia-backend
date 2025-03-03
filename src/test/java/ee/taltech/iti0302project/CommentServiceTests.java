package ee.taltech.iti0302project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ee.taltech.iti0302project.dto.CommentDto;
import ee.taltech.iti0302project.dto.PostDto;
import ee.taltech.iti0302project.dto.user.UserDto;
import ee.taltech.iti0302project.entity.CommentEntity;
import ee.taltech.iti0302project.entity.PostEntity;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.post.PostNotFoundException;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import ee.taltech.iti0302project.mapper.CommentMapper;
import ee.taltech.iti0302project.repository.CommentRepository;
import ee.taltech.iti0302project.repository.PostRepository;
import ee.taltech.iti0302project.service.CommentService;
import ee.taltech.iti0302project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class CommentServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Long postId = 1L;
    private String content = "Test comment content";
    private LocalDateTime now = LocalDateTime.now();

    private PostEntity post;
    private UserEntity user;
    private CommentDto expectedDto;

    private PostDto postDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        post = new PostEntity();
        post.setId(postId);

        user = new UserEntity();
        user.setId(1L);

        postDto = new PostDto();
        postDto.setId(postId);

        userDto = new UserDto();
        userDto.setId(1L);

        expectedDto = new CommentDto();
        expectedDto.setContent(content);
        expectedDto.setPost(postDto);
        expectedDto.setUser(userDto);
        expectedDto.setCreatedAt(now);
    }

    @Test
    void testAddComment_Success() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getCurrentUser()).thenReturn(user);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPost(post);
        commentEntity.setUser(user);
        commentEntity.setContent(content);
        commentEntity.setCreatedAt(now);

        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toDto(commentEntity)).thenReturn(expectedDto);

        CommentDto result = commentService.addComment(postId, content);

        verify(postRepository).findById(postId);
        verify(userService).getCurrentUser();
        verify(commentRepository).save(any(CommentEntity.class));
        verify(commentMapper).toDto(commentEntity);

        assertNotNull(result);
        assertEquals(expectedDto.getContent(), result.getContent());
        assertEquals(expectedDto.getPost().getId(), result.getPost().getId());
        assertEquals(expectedDto.getUser().getId(), result.getUser().getId());
        assertEquals(expectedDto.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void testAddComment_PostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> commentService.addComment(postId, content));
    }

    @Test
    void testAddComment_NoCurrentUser() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getCurrentUser()).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> commentService.addComment(postId, content));
    }

    @Test
    void testAddComment_CommentSavedSuccessfully() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getCurrentUser()).thenReturn(user);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPost(post);
        commentEntity.setUser(user);
        commentEntity.setContent(content);
        commentEntity.setCreatedAt(now);

        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toDto(commentEntity)).thenReturn(expectedDto);

        CommentDto result = commentService.addComment(postId, content);

        assertNotNull(result);
        assertEquals(expectedDto.getContent(), result.getContent());
        assertEquals(expectedDto.getPost().getId(), result.getPost().getId());
        assertEquals(expectedDto.getUser().getId(), result.getUser().getId());
        assertEquals(expectedDto.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void testAddComment_WhenPostRepositoryReturnsNull() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> commentService.addComment(postId, content));
    }

    @Test
    void testGetCommentsByPostId_Success() {
        Long postId = 1L;

        PostEntity post = new PostEntity();
        post.setId(postId);

        CommentEntity comment1 = new CommentEntity();
        comment1.setId(1L);
        comment1.setContent("First comment");

        CommentEntity comment2 = new CommentEntity();
        comment2.setId(2L);
        comment2.setContent("Second comment");

        List<CommentEntity> comments = List.of(comment1, comment2);

        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId(1L);
        commentDto1.setContent("First comment");

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setId(2L);
        commentDto2.setContent("Second comment");

        List<CommentDto> expected = List.of(commentDto1, commentDto2);
        when(commentMapper.toDtoList(comments)).thenReturn(expected);

        List<CommentDto> result = commentService.getCommentsByPostId(postId);

        verify(commentRepository).findByPostId(postId);

        assertEquals(2, result.size());
        assertEquals("First comment", result.get(0).getContent());
        assertEquals("Second comment", result.get(1).getContent());
    }

    @Test
    void testGetCommentsByPostId_NoCommentsFound() {
        Long postId = 1L;
        when(commentRepository.findByPostId(postId)).thenReturn(List.of());
        List<CommentDto> result = commentService.getCommentsByPostId(postId);
        verify(commentRepository).findByPostId(postId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCommentsByPostId_PostNotFound() {
        Long postId = 1L;
        when(commentRepository.findByPostId(postId)).thenReturn(null);
        List<CommentDto> result = commentService.getCommentsByPostId(postId);
        verify(commentRepository).findByPostId(postId);
        assertTrue(result.isEmpty());
    }
}
