package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.RequestDto;
import com.DevSprint.voluntrix_backend.entities.SponsorshipRequest;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRequestRepository;

@Service
public class SponsorshipRequestService {
    private final SponsorshipRequestRepository requestRepo;

    public SponsorshipRequestService(SponsorshipRequestRepository requestRepo) {
        this.requestRepo = requestRepo;
    }

    public List<SponsorshipRequest> getAllRequests() {
        return requestRepo.findAll();
    }

    public SponsorshipRequest getRequestById(Long id) {
        return requestRepo.findById(id).orElse(null);
    }

    public RequestDto createRequest(SponsorshipRequest request) {
       
       
       SponsorshipRequest sponsorshipRequest = new SponsorshipRequest();
        sponsorshipRequest.setSponsor(request.getSponsor());
       
        return requestRepo.save(request);



    }

    public void deleteRequest(Long id) {
        requestRepo.deleteById(id);
    }

    public SponsorshipRequest RequestDto(RequestDto request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RequestDto'");
    }
}
