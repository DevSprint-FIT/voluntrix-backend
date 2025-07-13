package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.CommentDTO;
import com.DevSprint.voluntrix_backend.dtos.CreateCommentDTO;
import com.DevSprint.voluntrix_backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CreateCommentDTO dto) {
        CommentDTO createdComment = commentService.addComment(dto);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{socialFeedId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable Long socialFeedId) {
        List<CommentDTO> comments = commentService.getCommentsForPost(socialFeedId);
        return ResponseEntity.ok(comments);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
