package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.entities.ReactionEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.ReactionRepository;
import com.DevSprint.voluntrix_backend.repositories.SocialFeedRepository;
import com.DevSprint.voluntrix_backend.utils.ReactionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final SocialFeedRepository socialFeedRepository;

    @Transactional
    public ReactionDTO reactToPost(CreateReactionDTO dto, Long userId, UserType userType) {
        SocialFeedEntity feed = socialFeedRepository.findById(dto.getSocialFeedId())
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + dto.getSocialFeedId()));

        Optional<ReactionEntity> existingReaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(
                dto.getSocialFeedId(), userId, userType);

        ReactionEntity savedReaction;

        if (existingReaction.isPresent()) {
            ReactionEntity reaction = existingReaction.get();
            boolean wasReacted = reaction.isReacted();
            boolean nowReacted = !wasReacted;

            reaction.setReacted(nowReacted);

            // Update impressions
            if (wasReacted && !nowReacted && feed.getImpressions() > 0) {
                feed.setImpressions(feed.getImpressions() - 1);
            } else if (!wasReacted && nowReacted) {
                feed.setImpressions(feed.getImpressions() + 1);
            }

            savedReaction = reactionRepository.save(reaction);
        } else {
            // First-time like
            ReactionEntity newReaction = new ReactionEntity();
            newReaction.setSocialFeed(feed);
            newReaction.setUserId(userId);
            newReaction.setUserType(userType);
            newReaction.setReacted(true);
            newReaction.setCreatedAt(LocalDateTime.now());

            feed.setImpressions(feed.getImpressions() + 1);
            savedReaction = reactionRepository.save(newReaction);
        }

        socialFeedRepository.save(feed);
        return ReactionConverter.toDTO(savedReaction);
    }

    // Get all reactions for a post
    public List<ReactionDTO> getReactionsForPost(Long socialFeedId) {
        socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + socialFeedId));

        return reactionRepository.findBySocialFeedId(socialFeedId)
                .stream()
                .map(ReactionConverter::toDTO)
                .toList();
    }

    // Get a user's reaction on a specific post
    public ReactionStatusDTO getUserReaction(Long socialFeedId, Long userId, UserType userType) {

        // Check if social feed exists
        socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + socialFeedId));

        // Try to find the reaction
        return reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType)
                .map(reaction -> new ReactionStatusDTO(reaction.getUserId(), reaction.isReacted()))
                .orElse(new ReactionStatusDTO(userId, false)); // No reaction found
    }


    // Remove a reaction completely
    @Transactional
    public void removeReaction(Long socialFeedId, Long userId, UserType userType) {

        ReactionEntity reactionEntity = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found for socialFeedId: " + socialFeedId +
                        ", userId: " + userId + ", userType: " + userType));

        SocialFeedEntity feed = reactionEntity.getSocialFeed();

        // Decrease impressions if the reaction is liked
        if (reactionEntity.isReacted() && feed.getImpressions() > 0) {
            feed.setImpressions(feed.getImpressions() - 1);
        }

        reactionRepository.delete(reactionEntity);
        socialFeedRepository.save(feed);
    }

    // Delete reactions for a post when the post is deleted
    @Transactional
    public void deletePostWithReactions(Long socialFeedId) {
        socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + socialFeedId));

        // Only delete the post — reactions will be automatically deleted
        socialFeedRepository.deleteById(socialFeedId);

    }

}
