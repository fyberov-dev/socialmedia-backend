package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.PostDto;
import ee.taltech.iti0302project.entity.PostEntity;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.post.PostEditNoAccessException;
import ee.taltech.iti0302project.exceptions.post.PostNotFoundException;
import ee.taltech.iti0302project.mapper.PostMapper;
import ee.taltech.iti0302project.repository.PostRepository;
import ee.taltech.iti0302project.request.EditPostRequest;
import ee.taltech.iti0302project.response.FeedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePost() {
        PostDto postDto = new PostDto();
        PostEntity postEntity = new PostEntity();
        UserEntity userEntity = new UserEntity();

        when(postMapper.toEntity(postDto)).thenReturn(postEntity);
        when(postRepository.save(postEntity)).thenReturn(postEntity);
        when(postMapper.toDto(postEntity)).thenReturn(postDto);

        PostDto result = postService.savePost(postDto, userEntity);

        assertNotNull(result);
        verify(postRepository, times(1)).save(postEntity);
    }

    @Test
    void testDeletePost() {
        PostDto postDto = new PostDto();
        PostEntity postEntity = new PostEntity();

        when(postMapper.toEntity(postDto)).thenReturn(postEntity);

        postService.deletePost(postDto);

        verify(postRepository, times(1)).delete(postEntity);
    }

    @Test
    void testUpdatePost() {
        Long postId = 1L;
        EditPostRequest request = new EditPostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        PostEntity postEntity = new PostEntity();
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postRepository.save(postEntity)).thenReturn(postEntity);
        PostDto postDto = new PostDto();
        when(postMapper.toDto(postEntity)).thenReturn(postDto);

        PostDto result = postService.updatePost(postId, request);

        assertNotNull(result);
        assertEquals("Updated Title", postEntity.getTitle());
        assertEquals("Updated Content", postEntity.getContent());
        verify(postRepository, times(1)).save(postEntity);
    }

    @Test
    void testUpdatePostPostNotFound() {
        Long postId = 1L;
        EditPostRequest request = new EditPostRequest();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.updatePost(postId, request));
    }

    @Test
    void testFindById() {
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        PostDto postDto = new PostDto();

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postMapper.toDto(postEntity)).thenReturn(postDto);

        PostDto result = postService.findById(postId);

        assertNotNull(result);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void testFindByIdPostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findById(postId));
    }

    @Test
    void testGetPostsByUsername() {
        String username = "testUser";
        int pageNo = 0;
        int pageSize = 10;

        UserEntity user = new UserEntity();
        Page<PostEntity> postPage = new PageImpl<>(Collections.singletonList(new PostEntity()));
        FeedResponse expectedResponse = new FeedResponse();

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(postRepository.findAllByUser(eq(user), any(Pageable.class))).thenReturn(postPage);
        when(postMapper.toDtoList(postPage.getContent())).thenReturn(Collections.singletonList(new PostDto()));

        FeedResponse result = postService.getPostsByUsername(username, pageNo, pageSize);

        assertNotNull(result);
        verify(postRepository, times(1)).findAllByUser(eq(user), any(Pageable.class));
    }

    @Test
    void testDeletePostById() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);

        boolean result = postService.deletePostById(postId);

        assertTrue(result);
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void testDeletePostByIdPostNotFound() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        boolean result = postService.deletePostById(postId);

        assertFalse(result);
        verify(postRepository, never()).deleteById(postId);
    }

    @Test
    void testCheckPostOwnership() {
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        UserEntity currentUser = new UserEntity();

        currentUser.setId(1L);
        postEntity.setUser(currentUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        PostDto postDto = new PostDto();
        when(postMapper.toDto(postEntity)).thenReturn(postDto);

        PostDto result = postService.checkPostOwnership(postId);

        assertNotNull(result);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void testCheckPostOwnershipNoAccess() {
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        UserEntity currentUser = new UserEntity();
        UserEntity otherUser = new UserEntity();

        currentUser.setId(1L);
        otherUser.setId(2L);
        postEntity.setUser(otherUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThrows(PostEditNoAccessException.class, () -> postService.checkPostOwnership(postId));
    }

    @Test
    void testCreatePost() {
        PostDto postDto = new PostDto();
        PostEntity postEntity = new PostEntity();
        UserEntity userEntity = new UserEntity();

        when(postMapper.toEntity(postDto)).thenReturn(postEntity);

        postService.createPost(postDto, userEntity);

        verify(postMapper, times(1)).toEntity(postDto);
        verify(postRepository, times(1)).save(postEntity);
        assertEquals(userEntity, postEntity.getUser());
    }

    @Test
    void testFindAllWithPagination() {
        int pageNo = 0;
        int pageSize = 10;
        Page<PostEntity> postPage = new PageImpl<>(Collections.singletonList(new PostEntity()));
        List<PostDto> postDtos = Collections.singletonList(new PostDto());

        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);
        when(postMapper.toDtoList(postPage.getContent())).thenReturn(postDtos);

        FeedResponse result = postService.findAll(pageNo, pageSize);

        assertNotNull(result);
        assertEquals(1, result.getPosts().size());
        assertEquals(pageNo, result.getPageNo());
        assertEquals(pageSize, result.getPageSize());
        verify(postRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindAllWithoutPagination() {
        List<PostEntity> postEntities = Collections.singletonList(new PostEntity());
        List<PostDto> postDtos = Collections.singletonList(new PostDto());

        when(postRepository.findAll()).thenReturn(postEntities);
        when(postMapper.toDtoList(postEntities)).thenReturn(postDtos);

        List<PostDto> result = postService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository, times(1)).findAll();
    }
}