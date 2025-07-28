package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.CommentDTO;
import com.DevSprint.voluntrix_backend.dtos.CreateCommentDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.CommentService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    public ResponseEntity<CommentDTO> addComment(@RequestBody CreateCommentDTO dto) {
        Long userId = currentUserService.getCurrentUserId();
        UserType userType = currentUserService.getCurrentUserType();
        CommentDTO createdComment = commentService.addComment(dto, userId, userType);
        return ResponseEntity.ok(createdComment);
    }
    
    @GetMapping("/{socialFeedId}/all-comments")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable Long socialFeedId) {
        List<CommentDTO> comments = commentService.getCommentsForPost(socialFeedId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
