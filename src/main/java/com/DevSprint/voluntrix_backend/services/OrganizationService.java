package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.utils.OrganizationDTOConverter;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationDTOConverter organizationDTOConverter;

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationDTOConverter.toOrganizationDTOList(organizationRepository.findAll());
    }

    public OrganizationDTO getOrganizationDetails(Long id) {
        return organizationRepository.findById(id)
                .map(organizationDTOConverter::toOrganizationDTO)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));
    }

    public OrganizationDTO getOrganizationByUsername(String username) {
        return organizationRepository.findByUsername(username)
                .map(organizationDTOConverter::toOrganizationDTO)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with username: " + username));
    }


    public OrganizationDTO createOrganization(OrganizationCreateDTO organizationCreateDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Check if user already has an organization profile
        if (organizationRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("User already has an organization profile");
        }
        
        OrganizationEntity organization = organizationDTOConverter.toOrganizationEntity(organizationCreateDTO, user);
        OrganizationEntity savedOrganization = organizationRepository.save(organization);
        
        // Update user's profile completion status
        user.setIsProfileCompleted(true);
        userRepository.save(user);
        
        return organizationDTOConverter.toOrganizationDTO(savedOrganization);
    }

    /**
     * Get organization profile by user ID from the JWT token
     * 
     * @param userId The ID of the current authenticated user
     * @return OrganizationDTO containing the organization's profile information
     * @throws OrganizationNotFoundException if the organization profile doesn't exist
     */
    public OrganizationDTO getOrganizationByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        OrganizationEntity organization = organizationRepository.findByUser(user)
            .orElseThrow(() -> new OrganizationNotFoundException("Organization profile not found for user"));
        
        return organizationDTOConverter.toOrganizationDTO(organization);
    }

    /**
     * Update organization profile using JWT credentials
     * 
     * @param organizationUpdateDTO The update data
     * @param userId The ID of the current authenticated user
     * @return OrganizationDTO containing the updated organization information
     * @throws OrganizationNotFoundException if the organization profile doesn't exist
     */
    public OrganizationDTO updateOrganizationProfile(OrganizationUpdateDTO organizationUpdateDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        OrganizationEntity organization = organizationRepository.findByUser(user)
            .orElseThrow(() -> new OrganizationNotFoundException("Organization profile not found for user"));
        
        // Update only the fields provided in the DTO
        if (organizationUpdateDTO.getName() != null) {
            organization.setName(organizationUpdateDTO.getName());
        }
        if (organizationUpdateDTO.getDescription() != null) {
            organization.setDescription(organizationUpdateDTO.getDescription());
        }
        if (organizationUpdateDTO.getWebsite() != null) {
            organization.setWebsite(organizationUpdateDTO.getWebsite());
        }
        if (organizationUpdateDTO.getPhone() != null) {
            organization.setPhone(organizationUpdateDTO.getPhone());
        }
        if (organizationUpdateDTO.getBankName() != null) {
            organization.setBankName(organizationUpdateDTO.getBankName());
        }
        if (organizationUpdateDTO.getAccountNumber() != null) {
            organization.setAccountNumber(organizationUpdateDTO.getAccountNumber());
        }
        if (organizationUpdateDTO.getImageUrl() != null) {
            organization.setImageUrl(organizationUpdateDTO.getImageUrl());
        }
        if (organizationUpdateDTO.getFacebookLink() != null) {
            organization.setFacebookLink(organizationUpdateDTO.getFacebookLink());
        }
        if (organizationUpdateDTO.getLinkedinLink() != null) {
            organization.setLinkedinLink(organizationUpdateDTO.getLinkedinLink());
        }
        if (organizationUpdateDTO.getInstagramLink() != null) {
            organization.setInstagramLink(organizationUpdateDTO.getInstagramLink());
        }
        
        OrganizationEntity updatedOrganization = organizationRepository.save(organization);
        return organizationDTOConverter.toOrganizationDTO(updatedOrganization);
    }


    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        return organizationRepository.findById(id)
                .map(existingOrg -> {
                    organizationDTOConverter.updateEntityFromDTO(organizationDTO, existingOrg);
                    OrganizationEntity updatedOrg = organizationRepository.save(existingOrg);
                    return organizationDTOConverter.toOrganizationDTO(updatedOrg);
                })
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));
    }


    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new OrganizationNotFoundException("Organization not found with id: " + id);
        }
        organizationRepository.deleteById(id);
    }
}
