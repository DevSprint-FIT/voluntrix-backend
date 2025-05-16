package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.entities.ReactionEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.exceptions.InvalidUserTypeException;
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
    public ReactionDTO reactToPost(CreateReactionDTO dto) {
        SocialFeedEntity feed = socialFeedRepository.findById(dto.getSocialFeedId())
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + dto.getSocialFeedId()));

        UserType userType = parseUserType(dto.getUserType());

        Optional<ReactionEntity> existingReaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(
                dto.getSocialFeedId(), dto.getUserId(), userType);

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
            newReaction.setUserId(dto.getUserId());
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
        SocialFeedEntity post = socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + socialFeedId));

        return reactionRepository.findBySocialFeedId(socialFeedId)
                .stream()
                .map(ReactionConverter::toDTO)
                .toList();
    }

    // Get a user's reaction on a specific post
    public ReactionStatusDTO getUserReaction(Long socialFeedId, Long userId, String userTypeStr) {
        UserType userType = parseUserType(userTypeStr);

        // Check if social feed exists
        socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + socialFeedId));

        // Find the reaction for the user on the post
        ReactionEntity reaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType)
                .orElseThrow(() -> new ResourceNotFoundException("No reaction found for the user on the given post."));

        return new ReactionStatusDTO(reaction.getUserId(), reaction.isReacted());
    }

    // Remove a reaction completely
    @Transactional
    public void removeReaction(Long socialFeedId, Long userId, String userTypeStr) {
        UserType userType = parseUserType(userTypeStr);

        ReactionEntity reactionEntity = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found for socialFeedId: " + socialFeedId +
                        ", userId: " + userId + ", userType: " + userTypeStr));

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
        SocialFeedEntity feed = socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + socialFeedId));

        // Only delete the post — reactions will be automatically deleted
        socialFeedRepository.deleteById(socialFeedId);

    }

    // Utility method for parsing userType safely
    private UserType parseUserType(String userTypeStr) {
        try {
            return UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidUserTypeException("Invalid userType: " + userTypeStr);
        }
    }
}
