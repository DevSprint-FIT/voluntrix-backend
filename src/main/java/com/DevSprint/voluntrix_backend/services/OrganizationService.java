package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.Organization;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final EntityDTOConverter entityDTOConverter;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, EntityDTOConverter entityDTOConverter) {
        this.organizationRepository = organizationRepository;
        this.entityDTOConverter = entityDTOConverter;
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return entityDTOConverter.toOrganizationDTOList(organizationRepository.findAll());
    }

    public Optional<OrganizationDTO> getOrganizationDetails(Long id) {
        return organizationRepository.findById(id)
                .map(entityDTOConverter::toOrganizationDTO);
    }

    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = entityDTOConverter.toOrganizationEntity(organizationDTO);
        Organization savedOrganization = organizationRepository.save(organization);
        return entityDTOConverter.toOrganizationDTO(savedOrganization);
    }

    public Optional<OrganizationDTO> updateOrganization(Long id, OrganizationDTO updatedDTO) {
        return organizationRepository.findById(id).map(existingOrg -> {
            existingOrg.setName(updatedDTO.getName());
            existingOrg.setEmail(updatedDTO.getEmail());
            existingOrg.setPhone(updatedDTO.getPhone());
            existingOrg.setAccountNumber(updatedDTO.getAccountNumber());
            existingOrg.setIsVerified(updatedDTO.getIsVerified());
            existingOrg.setFollowerCount(updatedDTO.getFollowerCount());
            Organization updatedOrg = organizationRepository.save(existingOrg);
            return entityDTOConverter.toOrganizationDTO(updatedOrg);
        });
    }

    public boolean deleteOrganization(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
