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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;


    public PostDto savePost(PostDto postDto, UserEntity user) {
        PostEntity post = postMapper.toEntity(postDto);
        post.setUser(user);
        return postMapper.toDto(postRepository.save(post));
    }

    public void deletePost(PostDto postDto) {
        postRepository.delete(postMapper.toEntity(postDto));
    }

    public PostDto updatePost(Long id, EditPostRequest request) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postRepository.save(post);
        return postMapper.toDto(post);

    }

    public PostDto findById(Long id) {
        return postMapper.toDto(postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found")));
    }

    public FeedResponse getPostsByUsername(String username, int pageNo, int pageSize) {
        UserEntity user = userService.getUserByUsername(username);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostEntity> posts = postRepository.findAllByUser(user, pageable);
        return new FeedResponse()
                .setPosts(postMapper.toDtoList(posts.getContent()))
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setElementsTotal(posts.getTotalElements())
                .setPagesTotal(posts.getTotalPages())
                .setIsLast(posts.isLast());
    }

    public void createPost(PostDto postDTO, UserEntity userEntity) {
        PostEntity postEntity = postMapper.toEntity(postDTO);
        postEntity.setUser(userEntity);
        postRepository.save(postEntity);
    }

    public boolean deletePostById(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public FeedResponse findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostEntity> posts = postRepository.findAll(pageable);
        return new FeedResponse()
                .setPosts(postMapper.toDtoList(posts.getContent()))
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setElementsTotal(posts.getTotalElements())
                .setPagesTotal(posts.getTotalPages())
                .setIsLast(posts.isLast());
    }

    public List<PostDto> findAll() {
        return postMapper.toDtoList(postRepository.findAll());
    }


    public PostDto checkPostOwnership(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        UserEntity user = userService.getCurrentUser();
        if (post.getUser().getId() != user.getId()) {
            throw new PostEditNoAccessException("You cannot edit this post");
        }
        return postMapper.toDto(post);
    }
}
