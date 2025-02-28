package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.Organization;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationDetails(Long id) {
        return organizationRepository.findById(id);
    }

    public Organization createOrganization(Organization organization) {
        Optional<Organization> existingOrg = organizationRepository.findByEmail(organization.getEmail());
        if (existingOrg.isPresent()) {
            throw new IllegalArgumentException("Organization with this email already exists.");
        }
        return organizationRepository.save(organization);
    }

    public Optional<Organization> updateOrganization(Long id, Organization updatedOrganization){
        return organizationRepository.findById(id).map(existingOrg -> {
            existingOrg.setName(updatedOrganization.getName());
            existingOrg.setEmail(updatedOrganization.getEmail());
            existingOrg.setPhone(updatedOrganization.getPhone());
            return organizationRepository.save(existingOrg);
        });
    }

    public boolean deleteOrganization(Long id){
        if(organizationRepository.existsById(id)){
            organizationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}






