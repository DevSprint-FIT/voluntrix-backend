package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.CreateReactionDTO;
import com.DevSprint.voluntrix_backend.dtos.ReactionDTO;
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
    public ReactionDTO reactToPost(CreateReactionDTO dto){
        Optional<SocialFeedEntity> optionalFeed = socialFeedRepository.findById(dto.getSocialFeedId());
        if(optionalFeed.isEmpty()){
            throw new RuntimeException("Social feed post not found");
        }

        SocialFeedEntity feed = optionalFeed.get();

        UserType userType;
        try {
            userType = UserType.valueOf(dto.getUserType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid or missing userType: " + dto.getUserType());
        }

        Optional<ReactionEntity> existingReaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(
                dto.getSocialFeedId(), dto.getUserId(), userType
        );

        ReactionEntity savedReaction;

        if(existingReaction.isPresent()){
            ReactionEntity reaction = existingReaction.get();
            boolean wasReacted = reaction.isReacted();

            boolean nowReacted = !wasReacted;
            reaction.setReacted(nowReacted);

            // Update impressions count based on toggle
            if(wasReacted && !nowReacted){
                // Previously liked, now unliked
                if(feed.getImpressions() > 0){
                    feed.setImpressions(feed.getImpressions() - 1);
                }

            } else if(!wasReacted && nowReacted) {
                // Previously unliked, now liked
                feed.setImpressions(feed.getImpressions() + 1);
            }
            savedReaction = reactionRepository.save(reaction);
        } else {
            // First time like
            ReactionEntity newReaction = new ReactionEntity();
            newReaction.setSocialFeed(feed);
            newReaction.setUserId(dto.getUserId());
            newReaction.setUserType(userType);
            newReaction.setReacted(true); // Always true on creation
            newReaction.setCreatedAt(LocalDateTime.now());

            feed.setImpressions(feed.getImpressions() + 1);
            savedReaction = reactionRepository.save(newReaction);
        }

        socialFeedRepository.save(feed);

        return ReactionConverter.toDTO(savedReaction);
    }

    // Get all reactions for a post
    public List<ReactionDTO> getReactionsForPost(Long socialFeedId){
        List<ReactionEntity> reactions = reactionRepository.findBySocialFeedId(socialFeedId);
        return reactions.stream()
                .map(ReactionConverter::toDTO)
                .toList();
    }


    // Get a user's reaction on a specific post
    public ReactionDTO getUserReaction(Long socialFeedId, Long userId, String userTypeStr) {
        UserType userType;
        try {
            userType = UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidUserTypeException("Invalid userType: " + userTypeStr);
        }

        // Check if social feed exists
        SocialFeedEntity socialFeed = socialFeedRepository.findById(socialFeedId)
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + socialFeedId));

        // Find the reaction for the user on the post
        ReactionEntity reaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType)
                .orElseThrow(() -> new ResourceNotFoundException("No reaction found for the user on the given post."));

        return ReactionConverter.toDTO(reaction);
    }


    // Remove a reaction completely
    @Transactional
    public void removeReaction(Long socialFeedId, Long userId, String userTypeStr) {
        UserType userType;
        try {
            userType = UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid userType: " + userTypeStr);
        }

        Optional<ReactionEntity> reaction = reactionRepository.findBySocialFeedIdAndUserIdAndUserType(socialFeedId, userId, userType);
        if (reaction.isPresent()) {
            ReactionEntity reactionEntity = reaction.get();
            SocialFeedEntity feed = reactionEntity.getSocialFeed();

            // Decrease impressions if the reaction is liked
            if (reactionEntity.isReacted() && feed.getImpressions() > 0) {
                feed.setImpressions(feed.getImpressions() - 1);
            }

            reactionRepository.delete(reactionEntity);
            socialFeedRepository.save(feed);
        } else {
            throw new RuntimeException("Reaction not found to remove.");
        }
    }

    // Delete reactions for a post when the post is deleted
    @Transactional
    public void deletePostWithReactions(Long socialFeedId) {
        Optional<SocialFeedEntity> optionalFeed = socialFeedRepository.findById(socialFeedId);
        if (optionalFeed.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        // Only delete the post — reactions will be automatically deleted
        socialFeedRepository.deleteById(socialFeedId);
    }


}



