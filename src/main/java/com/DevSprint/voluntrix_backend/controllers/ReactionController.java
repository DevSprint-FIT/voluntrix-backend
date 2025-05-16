package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.services.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionDTO> reactToPost(@RequestBody CreateReactionDTO createReactionDTO){
        ReactionDTO updated = reactionService.reactToPost(createReactionDTO);
        return ResponseEntity.ok(updated);
    }

    // Get all reactions for a post
    @GetMapping("/{socialFeedId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsForPost(@PathVariable Long socialFeedId){
        List<ReactionDTO> reactions = reactionService.getReactionsForPost(socialFeedId);
        return ResponseEntity.ok(reactions);
    }

    // Get a user's reaction on a specific post
    @GetMapping("/{socialFeedId}/{userId}/{userType}")
    public ResponseEntity<ReactionStatusDTO> getUserReaction(@PathVariable Long socialFeedId,
                                                             @PathVariable Long userId,
                                                             @PathVariable String userType) {
        ReactionStatusDTO reaction = reactionService.getUserReaction(socialFeedId, userId, userType);
        return ResponseEntity.ok(reaction);
    }

    @DeleteMapping("/{socialFeedId}/{userId}/{userType}")
    public ResponseEntity<String> removeReaction(@PathVariable Long socialFeedId,
                                               @PathVariable Long userId,
                                               @PathVariable String userType){
        reactionService.removeReaction(socialFeedId, userId, userType);
        return ResponseEntity.ok("Reaction deleted successfully");
    }

}
