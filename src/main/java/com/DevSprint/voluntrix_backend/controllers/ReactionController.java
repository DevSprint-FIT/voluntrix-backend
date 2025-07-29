package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.ReactionService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/reactions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<ReactionDTO> reactToPost(@RequestBody CreateReactionDTO createReactionDTO){
        Long userId = currentUserService.getCurrentUserId();
        UserType userType = currentUserService.getCurrentUserType();
        ReactionDTO updated = reactionService.reactToPost(createReactionDTO, userId, userType);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{socialFeedId}/total-reactions")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    public ResponseEntity<List<ReactionDTO>> getReactionsForPost(@PathVariable Long socialFeedId){
        List<ReactionDTO> reactions = reactionService.getReactionsForPost(socialFeedId);
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/{socialFeedId}/my-reaction")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    public ResponseEntity<ReactionStatusDTO> getUserReaction(@PathVariable Long socialFeedId) {
        Long userId = currentUserService.getCurrentUserId();
        UserType userType = currentUserService.getCurrentUserType();
        ReactionStatusDTO reaction = reactionService.getUserReaction(socialFeedId, userId, userType);
        return ResponseEntity.ok(reaction);
    }

    @DeleteMapping("/{socialFeedId}/remove-reaction")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<String> removeReaction(@PathVariable Long socialFeedId) {
        Long userId = currentUserService.getCurrentUserId();
        UserType userType = currentUserService.getCurrentUserType();
        reactionService.removeReaction(socialFeedId, userId, userType);
        return ResponseEntity.ok("Reaction deleted successfully");
    }

}
