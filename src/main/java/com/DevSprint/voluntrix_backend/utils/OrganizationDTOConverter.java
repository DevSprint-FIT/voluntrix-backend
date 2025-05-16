package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;

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
        dto.setOrganizationName(socialFeed.getOrganization().getName());
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
        organizationDTO.setName(organization.getName());
        organizationDTO.setUsername(organization.getUsername());
        organizationDTO.setInstitute(organization.getInstitute());
        organizationDTO.setEmail(organization.getEmail());
        organizationDTO.setPhone(organization.getPhone());
        organizationDTO.setAccountNumber(AESUtil.decrypt(organization.getAccountNumber()));
        organizationDTO.setIsVerified(organization.getIsVerified());
        organizationDTO.setFollowerCount(organization.getFollowerCount());
        organizationDTO.setJoinedDate(organization.getCreatedAt());
        organizationDTO.setDescription(organization.getDescription());
        organizationDTO.setWebsite(organization.getWebsite());
        organizationDTO.setBankName(organization.getBankName());
        organization.setWebsite(organizationDTO.getWebsite());
        organization.setBankName(organizationDTO.getBankName());
        organizationDTO.setImageUrl(organization.getImageUrl());
        organizationDTO.setFacebookLink(organization.getFacebookLink());
        organizationDTO.setLinkedinLink(organization.getLinkedinLink());
        organizationDTO.setInstagramLink(organization.getInstagramLink());

        return organizationDTO;
    }

    // OrganizationDTO to Organization
    public OrganizationEntity toOrganizationEntity(OrganizationDTO organizationDTO) {
        var organization = new OrganizationEntity();

        organization.setId(organizationDTO.getId());
        organization.setName(organizationDTO.getName());
        organization.setUsername(organizationDTO.getUsername());
        organization.setInstitute(organizationDTO.getInstitute());
        organization.setEmail(organizationDTO.getEmail());
        organization.setPhone(organizationDTO.getPhone());
        organization.setAccountNumber(organizationDTO.getAccountNumber());
        organization.setIsVerified(organizationDTO.getIsVerified());
        organization.setFollowerCount(organizationDTO.getFollowerCount());
        organization.setCreatedAt(organizationDTO.getJoinedDate());
        organization.setDescription(organizationDTO.getDescription());
        organization.setImageUrl(organizationDTO.getImageUrl());
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

}
