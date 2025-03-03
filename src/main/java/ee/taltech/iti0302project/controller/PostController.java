package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.PostDto;
import ee.taltech.iti0302project.exceptions.InvalidUserException;
import ee.taltech.iti0302project.request.EditPostRequest;
import ee.taltech.iti0302project.response.FeedResponse;
import ee.taltech.iti0302project.service.PostService;
import ee.taltech.iti0302project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Operation(summary = "Create a new post", description = "Allows a logged-in user to create a new post.")
    @PostMapping("/posts")
    public ResponseEntity<PostDto> postPost(@RequestBody PostDto postDto) {
        if (userService.getCurrentUser() == null) {
            throw new InvalidUserException("User is not logged in");
        }
        return ResponseEntity.ok(postService.savePost(postDto, userService.getCurrentUser()));
    }

    @Operation(summary = "Retrieve all posts", description = "Fetches all posts. No authentication required.")
    @GetMapping("/posts")
    public ResponseEntity<FeedResponse> getPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(postService.findAll(pageNo, pageSize));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by its ID. Requires the post to exist.")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        PostDto postDto = postService.findById(id);
        if (postDto == null) {
            return ResponseEntity.noContent().build();
        }
        postService.deletePost(postDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{id}/edit")
    public ResponseEntity<PostDto> checkPostOwnership(@PathVariable Long id) {
        return ResponseEntity.ok(postService.checkPostOwnership(id));
    }

    @Operation(summary = "Update a post", description = "Updates the details of an existing post.")
    @PutMapping("/posts/{id}/edit")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody EditPostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }
}
