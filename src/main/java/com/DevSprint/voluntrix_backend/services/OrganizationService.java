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


    public OrganizationDTO createOrganization(OrganizationCreateDTO organizationCreateDTO) {
        OrganizationEntity organization = entityDTOConverter.toOrganizationEntity(organizationCreateDTO);
        OrganizationEntity savedOrganization = organizationRepository.save(organization);
        return entityDTOConverter.toOrganizationDTO(savedOrganization);
    }


    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        return organizationRepository.findById(id)
                .map(existingOrg -> {
                    entityDTOConverter.updateEntityFromDTO(organizationDTO, existingOrg);
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
