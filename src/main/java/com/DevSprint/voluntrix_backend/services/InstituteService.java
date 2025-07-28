package com.DevSprint.voluntrix_backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevSprint.voluntrix_backend.dtos.InstituteDTO;
import com.DevSprint.voluntrix_backend.entities.InstituteEntity;
import com.DevSprint.voluntrix_backend.exceptions.UserNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.InstituteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InstituteService {

    private final InstituteRepository instituteRepository;

    public InstituteDTO createInstitute(InstituteDTO instituteDTO) {
        log.info("Creating new institute: {}", instituteDTO.getName());

        // Check if institute with same key already exists
        if (instituteRepository.findByKey(instituteDTO.getKey()).isPresent()) {
            throw new IllegalArgumentException("Institute with key '" + instituteDTO.getKey() + "' already exists");
        }

        // Check if institute with same name already exists
        if (instituteRepository.findByName(instituteDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Institute with name '" + instituteDTO.getName() + "' already exists");
        }

        // Check if institute with same domain already exists
        if (instituteRepository.findByDomain(instituteDTO.getDomain()).isPresent()) {
            throw new IllegalArgumentException("Institute with domain '" + instituteDTO.getDomain() + "' already exists");
        }

        InstituteEntity instituteEntity = InstituteEntity.builder()
                .key(instituteDTO.getKey().toUpperCase())
                .name(instituteDTO.getName())
                .domain(instituteDTO.getDomain().toLowerCase())
                .build();

        InstituteEntity savedInstitute = instituteRepository.save(instituteEntity);
        log.info("Institute created successfully with key: {}", savedInstitute.getKey());
        
        return toInstituteDTO(savedInstitute);
    }

    @Transactional(readOnly = true)
    public List<InstituteDTO> getAllInstitutes() {
        log.info("Retrieving all institutes");
        List<InstituteEntity> institutes = instituteRepository.findAllOrderedByName();
        return institutes.stream()
                .map(this::toInstituteDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstituteDTO getInstituteByKey(String key) {
        log.info("Retrieving institute with key: {}", key);
        InstituteEntity institute = instituteRepository.findByKey(key.toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Institute not found with key: " + key));
        return toInstituteDTO(institute);
    }

    public InstituteDTO updateInstitute(String key, InstituteDTO instituteDTO) {
        log.info("Updating institute with key: {}", key);

        InstituteEntity existingInstitute = instituteRepository.findByKey(key.toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Institute not found with key: " + key));

        // Check if another institute with the same name exists (excluding current institute)
        instituteRepository.findByName(instituteDTO.getName())
                .filter(institute -> !institute.getKey().equals(existingInstitute.getKey()))
                .ifPresent(institute -> {
                    throw new IllegalArgumentException("Institute with name '" + instituteDTO.getName() + "' already exists");
                });

        // Check if another institute with the same domain exists (excluding current institute)
        instituteRepository.findByDomain(instituteDTO.getDomain().toLowerCase())
                .filter(institute -> !institute.getKey().equals(existingInstitute.getKey()))
                .ifPresent(institute -> {
                    throw new IllegalArgumentException("Institute with domain '" + instituteDTO.getDomain() + "' already exists");
                });

        // Update fields
        existingInstitute.setName(instituteDTO.getName());
        existingInstitute.setDomain(instituteDTO.getDomain().toLowerCase());

        InstituteEntity updatedInstitute = instituteRepository.save(existingInstitute);
        log.info("Institute updated successfully with key: {}", updatedInstitute.getKey());
        
        return toInstituteDTO(updatedInstitute);
    }

    private InstituteDTO toInstituteDTO(InstituteEntity entity) {
        return new InstituteDTO(
                entity.getKey(),
                entity.getName(),
                entity.getDomain()
        );
    }
}
