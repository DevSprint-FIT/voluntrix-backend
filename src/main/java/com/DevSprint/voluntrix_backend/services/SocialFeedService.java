package com.DevSprint.voluntrix_backend.services;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SocialFeedRepository;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DevSprint.voluntrix_backend.enums.MediaType;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialFeedService {
    private final SocialFeedRepository socialFeedRepository;
    private final OrganizationRepository organizationRepository;
    private final EntityDTOConverter entityDTOConverter;

    @Autowired
    public SocialFeedService(SocialFeedRepository socialFeedRepository,
                             OrganizationRepository organizationRepository,
                             EntityDTOConverter entityDTOConverter){
        this.socialFeedRepository = socialFeedRepository;
        this.organizationRepository = organizationRepository;
        this.entityDTOConverter = entityDTOConverter;
    }

    public SocialFeedResponseDTO createPost(SocialFeedRequestDTO socialFeedRequestDTO){

        OrganizationEntity organization = organizationRepository.findById(socialFeedRequestDTO.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with is: " + socialFeedRequestDTO.getOrganizationId()));

        SocialFeedEntity post = new SocialFeedEntity();
        post.setOrganization(organization);
        post.setContent(socialFeedRequestDTO.getContent());
        if (socialFeedRequestDTO.getMediaType() == null || socialFeedRequestDTO.getMediaType().equals(MediaType.NONE)) {
            post.setMediaUrl(null);
            post.setMediaSizeInBytes(null);
        } else {
            post.setMediaUrl(socialFeedRequestDTO.getMediaUrl());
            post.setMediaSizeInBytes(socialFeedRequestDTO.getMediaSizeInBytes());
        }
        post.setMediaType(socialFeedRequestDTO.getMediaType());

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setMediaSizeInBytes(socialFeedRequestDTO.getMediaSizeInBytes());

        SocialFeedEntity savedPost = socialFeedRepository.save(post);
        return entityDTOConverter.toSocialFeedResponseDTO(savedPost);

    }

    public List<SocialFeedResponseDTO> getPostsByOrganizationId(Long organizationId){
        // Check if the organization exists
        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with ID: " + organizationId));

        // If the organization exists
        List<SocialFeedEntity> posts = socialFeedRepository.findByOrganizationId(organizationId);

        // If no posts are found for the organization
        if(posts.isEmpty()){
            throw new ResourceNotFoundException("No posts found for organization with ID: " + organizationId);
        }

        return posts.stream()
                .map(entityDTOConverter::toSocialFeedResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SocialFeedResponseDTO> getAllPosts(){
        return socialFeedRepository.findAll().stream()
                .map(entityDTOConverter::toSocialFeedResponseDTO)
                .collect(Collectors.toList());
    }

    public void deletePostById(Long postId){
        if(!socialFeedRepository.existsById(postId)){
             throw new ResourceNotFoundException("Post not found with ID: " + postId);
        }
        socialFeedRepository.deleteById(postId);
    }

    public SocialFeedEntity updatePost(Long id, SocialFeedUpdateDTO updateDTO){
        SocialFeedEntity post = socialFeedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + id));

        boolean contentOrMediaChanged = false;

        if (updateDTO.getContent() != null) {
            post.setContent(updateDTO.getContent());
            contentOrMediaChanged = true;
        }
        if(updateDTO.getMediaUrl() != null){
            post.setMediaUrl(updateDTO.getMediaUrl());
            contentOrMediaChanged = true;
        }

        if(updateDTO.getImpressions() != null){
            post.setImpressions(updateDTO.getImpressions());
        }

        if(contentOrMediaChanged){
            post.setUpdatedAt(LocalDateTime.now());
        }
        return socialFeedRepository.save(post);
    }

}
