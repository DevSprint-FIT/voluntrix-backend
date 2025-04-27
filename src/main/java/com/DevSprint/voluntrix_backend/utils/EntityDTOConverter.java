package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.Organization;

@Component
public class EntityDTOConverter {

    // Organization to OrganizationDTO
    public OrganizationDTO toOrganizationDTO(Organization organization) {
        var organizationDTO = new OrganizationDTO();

        organizationDTO.setId(organization.getId());
        organizationDTO.setName(organization.getName());
        organizationDTO.setUsername(organization.getUsername());
        organizationDTO.setInstitute(organization.getInstitute());
        organizationDTO.setEmail(organization.getEmail());
        organizationDTO.setPhone(organization.getPhone());
        organizationDTO.setAccountNumber(organization.getAccountNumber());
        organizationDTO.setIsVerified(organization.getIsVerified());
        organizationDTO.setFollowerCount(organization.getFollowerCount());
        organizationDTO.setJoinedDate(organization.getCreatedAt());
        organizationDTO.setDescription(organization.getDescription());
        organizationDTO.setWebsite(organization.getWebsite());
        organizationDTO.setBankName(organization.getBankName());
        organization.setWebsite(organizationDTO.getWebsite());
        organization.setBankName(organizationDTO.getBankName());
        organizationDTO.setImageUrl(organization.getImageUrl());

        return organizationDTO;
    }

    // OrganizationDTO to Organization
    public Organization toOrganizationEntity(OrganizationDTO organizationDTO) {
        var organization = new Organization();

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

        return organization;
    }

    // List<Organization> to List<OrganizationDTO>
    public List<OrganizationDTO> toOrganizationDTOList(List<Organization> organizationList) {
        return organizationList.stream()
                .map(this::toOrganizationDTO)
                .collect(Collectors.toList());
    }
}
