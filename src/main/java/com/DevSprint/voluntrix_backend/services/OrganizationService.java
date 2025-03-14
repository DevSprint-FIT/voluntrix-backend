package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.Organization;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrganizationDTO> getOrganizationDetails(Long id) {
        return organizationRepository.findById(id)
                .map(this::mapToDTO);
    }

    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = mapToEntity(organizationDTO);
        Organization savedOrganization = organizationRepository.save(organization);
        return mapToDTO(savedOrganization);
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
            return mapToDTO(updatedOrg);
        });
    }

    public boolean deleteOrganization(Long id){
        if(organizationRepository.existsById(id)){
            organizationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //Mapping methods
    private OrganizationDTO mapToDTO(Organization org){
        return new OrganizationDTO(
                org.getId(), org.getName(), org.getEmail(), org.getPhone(),
                org.getAccountNumber(), org.getIsVerified(), org.getFollowerCount()
        );
    }

    private Organization mapToEntity(OrganizationDTO dto){
        return new Organization(
                dto.getId(), dto.getName(), dto.getEmail(), dto.getPhone(),
                dto.getAccountNumber(), dto.getIsVerified(), dto.getFollowerCount()
        );
    }
}






