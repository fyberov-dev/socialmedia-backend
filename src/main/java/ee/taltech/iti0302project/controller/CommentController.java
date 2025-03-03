package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.CommentDto;
import ee.taltech.iti0302project.entity.CommentEntity;
import ee.taltech.iti0302project.request.CommentRequest;
import ee.taltech.iti0302project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(postId, request.getContent()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}
