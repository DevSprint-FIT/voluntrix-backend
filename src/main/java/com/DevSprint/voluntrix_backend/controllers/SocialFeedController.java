package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SocialFeedRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.services.SocialFeedService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.utils.OrganizationDTOConverter;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/social-feed")
public class SocialFeedController {

    private final SocialFeedService socialFeedService;
    private final OrganizationDTOConverter organizationDTOConverter;
    private final CurrentUserService currentUserService;

    @RequiresRole(UserType.ORGANIZATION)
    @PostMapping
    public SocialFeedResponseDTO createPost(@RequestBody SocialFeedRequestDTO requestDTO){
        Long organizationId = currentUserService.getCurrentEntityId();
        return socialFeedService.createPost(requestDTO, organizationId);
    }

    @RequiresRole(UserType.ORGANIZATION)
    @GetMapping("/my-posts")
    public ResponseEntity<List<SocialFeedResponseDTO>> getPostsByOrganization(){
        Long organizationId = currentUserService.getCurrentEntityId();
        List<SocialFeedResponseDTO> posts = socialFeedService.getPostsByOrganizationId(organizationId);
        return ResponseEntity.ok(posts);
    }

    @RequiresRole(UserType.ORGANIZATION)
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        socialFeedService.deletePostById(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @RequiresRole(UserType.ORGANIZATION)
    @PatchMapping("/{id}")
    public ResponseEntity<SocialFeedResponseDTO> updatePost(@PathVariable Long id, @RequestBody SocialFeedUpdateDTO socialFeedUpdateDTO) {
        SocialFeedEntity updatedPost = socialFeedService.updatePost(id, socialFeedUpdateDTO);
        SocialFeedResponseDTO responseDTO = organizationDTOConverter.toSocialFeedResponseDTO(updatedPost);  // Use injected instance here
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
