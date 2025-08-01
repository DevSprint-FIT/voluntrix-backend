package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganizationDTOConverter {
    public SocialFeedResponseDTO toSocialFeedResponseDTO(SocialFeedEntity socialFeed){
        SocialFeedResponseDTO dto = new SocialFeedResponseDTO();
        dto.setId(socialFeed.getId());
        dto.setContent(socialFeed.getContent());
        dto.setMediaUrl(socialFeed.getMediaUrl());
        dto.setMediaType(socialFeed.getMediaType());
        dto.setCreatedAt(socialFeed.getCreatedAt());
        dto.setUpdatedAt(socialFeed.getUpdatedAt());
        dto.setOrganizationName(socialFeed.getOrganization().getUser().getFullName()); // Get name from UserEntity
        dto.setInstitute(socialFeed.getOrganization().getInstitute());
        dto.setOrganizationImageUrl(socialFeed.getOrganization().getImageUrl());
        dto.setImpressions(socialFeed.getImpressions());
        dto.setShares(socialFeed.getShares());

        return dto;

    }

    public List<SocialFeedResponseDTO> toSocialFeedResponseDTOList(List<SocialFeedEntity> socialFeeds){
        return socialFeeds.stream()
                .map(this::toSocialFeedResponseDTO)
                .collect(Collectors.toList());
    }

    // Organization to OrganizationDTO
    public OrganizationDTO toOrganizationDTO(OrganizationEntity organization) {
        var organizationDTO = new OrganizationDTO();

        organizationDTO.setId(organization.getId());
        
        // Map from associated UserEntity
        organizationDTO.setName(organization.getUser().getFullName());
        organizationDTO.setUsername(organization.getUser().getHandle());
        organizationDTO.setEmail(organization.getUser().getEmail());
        
        // Map organization-specific fields
        organizationDTO.setInstitute(organization.getInstitute());
        organizationDTO.setPhone(organization.getPhone());
        organizationDTO.setAccountNumber(organization.getAccountNumber() != null ? 
            AESUtil.decrypt(organization.getAccountNumber()) : null);
        organizationDTO.setIsVerified(organization.getIsVerified());
        organizationDTO.setFollowerCount(organization.getFollowerCount());
        organizationDTO.setJoinedDate(organization.getCreatedAt());
        organizationDTO.setDescription(organization.getDescription());
        organizationDTO.setWebsite(organization.getWebsite());
        organizationDTO.setBankName(organization.getBankName());
        organization.setWebsite(organizationDTO.getWebsite());
        organization.setBankName(organizationDTO.getBankName());
        organizationDTO.setImageUrl(organization.getImageUrl());
        organizationDTO.setDocumentUrl(organization.getDocumentUrl());
        organizationDTO.setFacebookLink(organization.getFacebookLink());
        organizationDTO.setLinkedinLink(organization.getLinkedinLink());
        organizationDTO.setInstagramLink(organization.getInstagramLink());

        return organizationDTO;
    }

    // OrganizationDTO to Organization
    public OrganizationEntity toOrganizationEntity(OrganizationDTO organizationDTO) {
        var organization = new OrganizationEntity();

        organization.setId(organizationDTO.getId());
        // Note: name, username, email are inherited from UserEntity relationship
        organization.setInstitute(organizationDTO.getInstitute());
        organization.setPhone(organizationDTO.getPhone());
        organization.setAccountNumber(organizationDTO.getAccountNumber() != null ? 
        AESUtil.encrypt(organizationDTO.getAccountNumber()) : null);
        organization.setIsVerified(organizationDTO.getIsVerified());
        organization.setFollowerCount(organizationDTO.getFollowerCount());
        organization.setCreatedAt(organizationDTO.getJoinedDate());
        organization.setDescription(organizationDTO.getDescription());
        organization.setImageUrl(organizationDTO.getImageUrl());
        organization.setDocumentUrl(organizationDTO.getDocumentUrl());
        organization.setFacebookLink(organizationDTO.getFacebookLink());
        organization.setLinkedinLink(organizationDTO.getLinkedinLink());
        organization.setInstagramLink(organizationDTO.getInstagramLink());

        return organization;
    }

    // List<Organization> to List<OrganizationDTO>
    public List<OrganizationDTO> toOrganizationDTOList(List<OrganizationEntity> organizationList) {
        return organizationList.stream()
                .map(this::toOrganizationDTO)
                .collect(Collectors.toList());
    }
  
    // OrganizationCreateDTO to OrganizationEntity
    public OrganizationEntity toOrganizationEntity(OrganizationCreateDTO dto) {
        OrganizationEntity entity = new OrganizationEntity();

        // Note: name, username, email are inherited from UserEntity relationship
        entity.setInstitute(dto.getInstitute());
        entity.setPhone(dto.getPhone());
        entity.setAccountNumber(dto.getAccountNumber() != null ? AESUtil.encrypt(dto.getAccountNumber()) : null);
        entity.setDescription(dto.getDescription());
        entity.setWebsite(dto.getWebsite());
        entity.setBankName(dto.getBankName());
        entity.setImageUrl(dto.getImageUrl());
        entity.setDocumentUrl(dto.getDocumentUrl());
        entity.setFacebookLink(dto.getFacebookLink());
        entity.setLinkedinLink(dto.getLinkedinLink());
        entity.setInstagramLink(dto.getInstagramLink());
        
        // Set default values
        entity.setIsVerified(false);
        entity.setFollowerCount(0);

        return entity;
    }

    // OrganizationCreateDTO to OrganizationEntity with User
    public OrganizationEntity toOrganizationEntity(OrganizationCreateDTO dto, UserEntity user) {
        OrganizationEntity entity = new OrganizationEntity();

        // Note: name, username, email are inherited from UserEntity relationship
        // and accessed via entity.getUser().getXxx() 
        
        // Set properties from DTO
        entity.setPhone(dto.getPhone());
        entity.setInstitute(dto.getInstitute());
        entity.setAccountNumber(dto.getAccountNumber() != null ? AESUtil.encrypt(dto.getAccountNumber()) : null);
        entity.setDescription(dto.getDescription());
        entity.setWebsite(dto.getWebsite());
        entity.setImageUrl(dto.getImageUrl());
        entity.setDocumentUrl(dto.getDocumentUrl());
        entity.setBankName(dto.getBankName());
        entity.setFacebookLink(dto.getFacebookLink());
        entity.setLinkedinLink(dto.getLinkedinLink());
        entity.setInstagramLink(dto.getInstagramLink());
        
        // Set default values
        entity.setIsVerified(false);
        entity.setFollowerCount(0);
        
        // Set the user relationship
        entity.setUser(user);

        return entity;
    }

    public void updateEntityFromDTO(OrganizationDTO dto, OrganizationEntity entity) {
        // Note: name, username, email cannot be updated directly as they come from UserEntity
        
        if (dto.getInstitute() != null) {
            entity.setInstitute(dto.getInstitute());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getAccountNumber() != null) {
            entity.setAccountNumber(AESUtil.encrypt(dto.getAccountNumber()));
        }
        if (dto.getImageUrl() != null) {
            entity.setImageUrl(dto.getImageUrl());
        }
        if (dto.getIsVerified() != null) {
            entity.setIsVerified(dto.getIsVerified());
        }
        if (dto.getFollowerCount() != null) {
            entity.setFollowerCount(dto.getFollowerCount());
        }
        if (dto.getWebsite() != null) {
            entity.setWebsite(dto.getWebsite());
        }
        if (dto.getBankName() != null) {
            entity.setBankName(dto.getBankName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getImageUrl() != null) {
            entity.setImageUrl(dto.getImageUrl());
        }
        if (dto.getFacebookLink() != null) {
            entity.setFacebookLink(dto.getFacebookLink());
        }
        if (dto.getLinkedinLink() != null) {
            entity.setLinkedinLink(dto.getLinkedinLink());
        }
        if (dto.getInstagramLink() != null) {
            entity.setInstagramLink(dto.getInstagramLink());
        }

    }
}
