package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SocialFeedRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.services.SocialFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/public/social-feed")
public class SocialFeedController {

    private final SocialFeedService socialFeedService;

    @Autowired
    public SocialFeedController(SocialFeedService socialFeedService){
        this.socialFeedService = socialFeedService;
    }

    @PostMapping
    public SocialFeedResponseDTO createPost(@RequestBody SocialFeedRequestDTO requestDTO){
        return socialFeedService.createPost(requestDTO);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<SocialFeedResponseDTO>> getPostsByOrganization(@PathVariable Long organizationId){
        List<SocialFeedResponseDTO> posts = socialFeedService.getPostsByOrganizationId(organizationId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping
    public ResponseEntity<List<SocialFeedResponseDTO>> getAllPosts(){
        List<SocialFeedResponseDTO> posts = socialFeedService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        socialFeedService.deletePostById(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SocialFeedEntity> updatePost(@PathVariable Long id, @RequestBody SocialFeedUpdateDTO socialFeedUpdateDTO){
        SocialFeedEntity updatedPost = socialFeedService.updatePost(id, socialFeedUpdateDTO);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
}
