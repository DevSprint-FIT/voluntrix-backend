package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.CommentDTO;
import com.DevSprint.voluntrix_backend.dtos.CreateCommentDTO;
import com.DevSprint.voluntrix_backend.entities.CommentEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.exceptions.InvalidUserTypeException;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.CommentRepository;
import com.DevSprint.voluntrix_backend.repositories.SocialFeedRepository;
import com.DevSprint.voluntrix_backend.utils.CommentDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SocialFeedRepository socialFeedRepository;
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;
    private final CommentDTOConverter commentDTOConverter;

    public CommentDTO addComment(CreateCommentDTO dto, Long userId, UserType userType) {
        SocialFeedEntity feed = socialFeedRepository.findById(dto.getSocialFeedId())
                .orElseThrow(() -> new ResourceNotFoundException("Social feed post not found with ID: " + dto.getSocialFeedId()));

        String commenterName;
        String profileImageUrl;

        // Validate the user BEFORE saving the comment
        switch (userType) {
            case VOLUNTEER -> {
                var volunteerDTO = volunteerService.getVolunteerByUsername(dto.getUserUsername());
                commenterName = volunteerDTO.getFullName();
                profileImageUrl = volunteerDTO.getProfilePictureUrl();
            }
            case ORGANIZATION -> {
                var organizationDTO = organizationService.getOrganizationByUsername(dto.getUserUsername());
                commenterName = organizationDTO.getName();
                profileImageUrl = organizationDTO.getImageUrl();
            }
            default -> throw new InvalidUserTypeException("Invalid userType: " + dto.getUserType());
        }

        // Only after validation passes, convert and save the comment
        CommentEntity comment = commentDTOConverter.toEntity(dto, feed);
        CommentEntity savedComment = commentRepository.save(comment);

        return commentDTOConverter.toDTO(savedComment, commenterName, profileImageUrl);
    }

    public List<CommentDTO> getCommentsForPost(Long socialFeedId) {
        List<CommentEntity> comments = commentRepository.findBySocialFeedIdOrderByCreatedAtAsc(socialFeedId);

        return comments.stream().map(comment -> {
            String commenterName;
            String profileImageUrl;

            switch (comment.getUserType()) {
                case VOLUNTEER -> {
                    var volunteerDTO = volunteerService.getVolunteerByUsername(comment.getUserUsername());
                    commenterName = volunteerDTO.getFullName();
                    profileImageUrl = volunteerDTO.getProfilePictureUrl();
                }
                case ORGANIZATION -> {
                    var organizationDTO = organizationService.getOrganizationByUsername(comment.getUserUsername());
                    commenterName = organizationDTO.getName();
                    profileImageUrl = organizationDTO.getImageUrl();
                }
                default -> throw new InvalidUserTypeException("Invalid userType: " + comment.getUserType());
            }

            return commentDTOConverter.toDTO(comment, commenterName, profileImageUrl);
        }).collect(Collectors.toList());
    }

    public void deleteComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));

        commentRepository.delete(comment);
    }
}