package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.PostDto;
import ee.taltech.iti0302project.response.FeedResponse;
import ee.taltech.iti0302project.service.PostService;
import ee.taltech.iti0302project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class FeedController {

    private final PostService postService;
    private final UserService userService;

    @Operation(summary = "Get user feed", description = "Fetches the feed for a specific user based on their username.")
    @GetMapping("/{username}")
    public ResponseEntity<FeedResponse> getUserFeed(
            @PathVariable String username,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(postService.getPostsByUsername(username, pageNo, pageSize));
    }

    @Operation(summary = "Create a new post", description = "Allows a logged-in user to create a new post.")
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostDto postDto) {
        if (userService.getCurrentUser() == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        postService.createPost(postDto, userService.getCurrentUser());
        return ResponseEntity.ok("Post created successfully!");
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by its ID. Requires the post to exist.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        boolean isDeleted = postService.deletePostById(postId);
        String message = isDeleted ? "Post deleted successfully!" : "Post could not be deleted!";
        return isDeleted ? ResponseEntity.ok(message) : ResponseEntity.status(400).body(message);
    }
}
