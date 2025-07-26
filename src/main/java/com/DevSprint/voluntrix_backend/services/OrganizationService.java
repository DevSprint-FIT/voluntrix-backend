package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.utils.OrganizationDTOConverter;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
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


    public OrganizationDTO createOrganization(OrganizationCreateDTO organizationCreateDTO) {
        OrganizationEntity organization = organizationDTOConverter.toOrganizationEntity(organizationCreateDTO);
        OrganizationEntity savedOrganization = organizationRepository.save(organization);
        return organizationDTOConverter.toOrganizationDTO(savedOrganization);
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
