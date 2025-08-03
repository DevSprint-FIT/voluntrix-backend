package com.DevSprint.voluntrix_backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.SponsorRequestTableDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
import com.DevSprint.voluntrix_backend.enums.SponsorshipPaymentStatus;
import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.SponsorNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.SponsorshipIsNotAvailableException;
import com.DevSprint.voluntrix_backend.exceptions.SponsorshipNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.SponsorshipRequestNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRequestRepository;
import com.DevSprint.voluntrix_backend.utils.SponsorshipRequestDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SponsorshipRequestService {

    private final SponsorshipRequestRepository sponsorshipRequestRepository;
    private final SponsorshipRepository sponsorshipRepository;
    private final SponsorshipRequestDTOConverter sponsorshipDTOConverter;
    private final SponsorRepository sponsorRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;

    public SponsorshipRequestDTO createSponsorshipRequest(SponsorshipRequestCreateDTO createDTO) {
        SponsorshipEntity sponsorship = sponsorshipRepository.findById(createDTO.getSponsorshipId())
                .orElseThrow(() -> new SponsorshipNotFoundException(
                        "Sponsorship not found with ID: " + createDTO.getSponsorshipId()));

        SponsorEntity sponsor = sponsorRepository.findById(createDTO.getSponsorId())
                .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found with ID: " + createDTO.getSponsorId()));

        // check availability
        if(!sponsorship.isAvailable()) {
            throw new SponsorshipIsNotAvailableException("Sponsorship is not available for requests.");
        }

        SponsorshipRequestEntity requestEntity = SponsorshipRequestDTOConverter.toSponsorshipRequestEntity(sponsor, sponsorship);

        SponsorshipRequestEntity savedRequest = sponsorshipRequestRepository.save(requestEntity);
        
        return sponsorshipDTOConverter.toSponsorshipRequestDTO(savedRequest);
    }

    public List<SponsorshipRequestDTO> getSponsorshipRequestsBySponsorId(Long sponsorId) {
        sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found with ID: " + sponsorId));

        List<SponsorshipRequestEntity> requests = sponsorshipRequestRepository.findBySponsor_SponsorId(sponsorId);
        
        return sponsorshipDTOConverter.toSponsorshipRequestDTOList(requests);
    }

    public List<SponsorshipRequestDTO> getSponsorshipRequestsByEventId(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<SponsorshipRequestEntity> requests = sponsorshipRequestRepository.findBySponsorship_Event_EventId(eventId);
        if (requests.isEmpty()) {
            throw new SponsorshipRequestNotFoundException("No sponsorship requests found for event ID: " + eventId);
        }

        return sponsorshipDTOConverter.toSponsorshipRequestDTOList(requests);
    }

    public List<SponsorRequestTableDTO> getSponsorshipRequestsBySponsorIdAndStatus(Long sponsorId, SponsorshipRequestStatus status) {
        sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found with ID: " + sponsorId));
    
        List<Object[]> sponsorshipDetails = sponsorshipRequestRepository.findEventDetailsWithSponsorshipBySponsorIdAndStatus(sponsorId, status);
        if (sponsorshipDetails.isEmpty()) {
            throw new SponsorshipRequestNotFoundException("No sponsorship requests found for sponsor ID: " + sponsorId + " with status: " + status);
        }

        Long eventId = (Long) sponsorshipDetails.get(0)[0];

        if (status.name() == "APPROVED") {
            Double totalAmountPaid = paymentRepository.sumTotalAmountPaidByEventIdAndSponsorId(eventId, sponsorId);
            SponsorshipPaymentStatus paymentStatus;

            if(totalAmountPaid == null) {
                totalAmountPaid = 0.0;
                paymentStatus = SponsorshipPaymentStatus.UNPAID;
            }else if (totalAmountPaid > (Integer) sponsorshipDetails.get(0)[4]) {
                throw new IllegalArgumentException("Total amount paid exceeds the sponsorship price.");
            } else if(totalAmountPaid < (Integer) sponsorshipDetails.get(0)[4]) {
                paymentStatus = SponsorshipPaymentStatus.PARTIALPAID;
            } else {
                paymentStatus = SponsorshipPaymentStatus.FULLPAID;
            }

            return sponsorshipDTOConverter.toSponsorRequestTableDTOList(sponsorshipDetails, paymentStatus, totalAmountPaid);

        }

        return sponsorshipDTOConverter.toSponsorRequestTableDTOList(sponsorshipDetails);
    }

    public List<SponsorshipRequestDTO> getSponsorshipRequestsByEventIdAndStatus(Long eventId, String status) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<SponsorshipRequestEntity> requests = sponsorshipRequestRepository.findBySponsorship_Event_EventId(eventId);
        if (requests.isEmpty()) {
            throw new SponsorshipRequestNotFoundException("No sponsorship requests found for event ID: " + eventId);
        }

        // Filter by status if needed
        if (!"ALL".equalsIgnoreCase(status)) {
            requests = requests.stream()
                    .filter(req -> req.getStatus().name().equals(status))
                    .collect(Collectors.toList());
        }

        return sponsorshipDTOConverter.toSponsorshipRequestDTOList(requests);
    }

    public SponsorshipRequestDTO updateSponsorshipRequestStatus(Long requestId, String status) {
        SponsorshipRequestEntity request = sponsorshipRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new SponsorshipRequestNotFoundException("Sponsorship request not found with ID: " + requestId));

        // Validate status
        if (!"PENDING".equalsIgnoreCase(status) && !"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        // Update status
        request.setStatus(SponsorshipRequestStatus.valueOf(status.toUpperCase()));
        SponsorshipRequestEntity updatedRequest = sponsorshipRequestRepository.save(request);

        return sponsorshipDTOConverter.toSponsorshipRequestDTO(updatedRequest);
    }

    public void deleteSponsorshipRequest(Long requestId) {
        SponsorshipRequestEntity request = sponsorshipRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new SponsorshipRequestNotFoundException("Sponsorship request not found with ID: " + requestId));
        sponsorshipRequestRepository.delete(request);
    }
}
