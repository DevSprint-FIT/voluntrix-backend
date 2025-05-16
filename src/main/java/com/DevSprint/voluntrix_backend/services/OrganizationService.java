package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.utils.AESUtil;
import com.DevSprint.voluntrix_backend.utils.OrganizationDTOConverter;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOConverter entityDTOConverter;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, OrganizationDTOConverter entityDTOConverter) {
        this.organizationRepository = organizationRepository;
        this.entityDTOConverter = entityDTOConverter;
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return entityDTOConverter.toOrganizationDTOList(organizationRepository.findAll());
    }

    public OrganizationDTO getOrganizationDetails(Long id) {
        return organizationRepository.findById(id)
                .map(entityDTOConverter::toOrganizationDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    }

    public OrganizationDTO getOrganizationByUsername(String username) {
        return organizationRepository.findByUsername(username)
                .map(entityDTOConverter::toOrganizationDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with username: " + username));
    }


    // Updated createOrganization method to use OrganizationCreateDTO
    public OrganizationDTO createOrganization(OrganizationCreateDTO organizationCreateDTO) {
        // Convert OrganizationCreateDTO to Organization entity
        OrganizationEntity organization = new OrganizationEntity();
        organization.setName(organizationCreateDTO.getName());
        organization.setUsername(organizationCreateDTO.getUsername());
        organization.setInstitute(organizationCreateDTO.getInstitute());
        organization.setEmail(organizationCreateDTO.getEmail());
        organization.setPhone(organizationCreateDTO.getPhone());
        organization.setAccountNumber(AESUtil.encrypt(organizationCreateDTO.getAccountNumber()));
        organization.setIsVerified(organizationCreateDTO.getIsVerified());
        organization.setFollowerCount(organizationCreateDTO.getFollowerCount());
        organization.setDescription(organizationCreateDTO.getDescription());
        organization.setWebsite(organizationCreateDTO.getWebsite());
        organization.setBankName(organizationCreateDTO.getBankName());
        organization.setFacebookLink(organizationCreateDTO.getFacebookLink());
        organization.setLinkedinLink(organizationCreateDTO.getLinkedinLink());
        organization.setInstagramLink(organizationCreateDTO.getInstagramLink());

        OrganizationEntity savedOrganization = organizationRepository.save(organization);
        return entityDTOConverter.toOrganizationDTO(savedOrganization);
    }

    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        return organizationRepository.findById(id)
                .map(existingOrg -> {
                    if (organizationDTO.getName() != null) {
                        existingOrg.setName(organizationDTO.getName());
                    }

                    if (organizationDTO.getInstitute() != null) {
                        existingOrg.setInstitute(organizationDTO.getInstitute());
                    }

                    if (organizationDTO.getEmail() != null) {
                        existingOrg.setEmail(organizationDTO.getEmail());
                    }

                    if (organizationDTO.getPhone() != null) {
                        existingOrg.setPhone(organizationDTO.getPhone());
                    }

                    if (organizationDTO.getAccountNumber() != null) {
                        existingOrg.setAccountNumber(organizationDTO.getAccountNumber());

                    }

                    if (organizationDTO.getIsVerified() != null) {
                        existingOrg.setIsVerified(organizationDTO.getIsVerified());
                    }

                    if (organizationDTO.getFollowerCount() != null) {
                        existingOrg.setFollowerCount(organizationDTO.getFollowerCount());
                    }

                    if (organizationDTO.getWebsite() != null) {
                        existingOrg.setWebsite(organizationDTO.getWebsite());
                    }

                    if (organizationDTO.getBankName() != null) {
                        existingOrg.setBankName(organizationDTO.getBankName());
                    }

                    if(organizationDTO.getDescription() != null) {
                        existingOrg.setDescription(organizationDTO.getDescription());
                    }

                    if(organizationDTO.getFacebookLink() != null){
                        existingOrg.setFacebookLink(organizationDTO.getFacebookLink());
                    }

                    if(organizationDTO.getLinkedinLink() != null){
                        existingOrg.setLinkedinLink(organizationDTO.getLinkedinLink());
                    }

                    if(organizationDTO.getInstagramLink() != null){
                        existingOrg.setInstagramLink(organizationDTO.getInstagramLink());
                    }


                    OrganizationEntity updatedOrg = organizationRepository.save(existingOrg);
                    return entityDTOConverter.toOrganizationDTO(updatedOrg);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    }

    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organization not found with id: " + id);
        }
        organizationRepository.deleteById(id);
    }
}
