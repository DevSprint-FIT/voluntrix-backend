package com.DevSprint.voluntrix_backend.config;

import com.DevSprint.voluntrix_backend.entities.UserSponsor;
import com.DevSprint.voluntrix_backend.repositories.UserSponsorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SponsorDataInitializer implements CommandLineRunner {

    private final UserSponsorRepository userSponsorRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userSponsorRepository.count() == 0) {
            log.info("Initializing sponsor data...");
            
            List<UserSponsor> sponsors = Arrays.asList(
                createSponsor("TechCorp_John", "John", "Anderson", "john.anderson@techcorp.com", 
                             "TechCorp Solutions", "+1-555-0101", "Senior Technology Sponsor specializing in educational initiatives", true),
                
                createSponsor("GreenEnergy_Sarah", "Sarah", "Wilson", "sarah.wilson@greenenergy.com", 
                             "Green Energy Inc", "+1-555-0102", "Environmental sustainability advocate and renewable energy sponsor", true),
                
                createSponsor("HealthFirst_Mike", "Michael", "Johnson", "mike.johnson@healthfirst.org", 
                             "HealthFirst Foundation", "+1-555-0103", "Healthcare industry sponsor focused on community wellness programs", false),
                
                createSponsor("EduSupport_Lisa", "Lisa", "Chen", "lisa.chen@edusupport.org", 
                             "Educational Support Network", "+1-555-0104", "Educational technology and scholarship sponsor", true),
                
                createSponsor("CommunityBank_Robert", "Robert", "Davis", "robert.davis@communitybank.com", 
                             "Community First Bank", "+1-555-0105", "Financial services sponsor supporting local community projects", true),
                
                createSponsor("InnovateLab_Emma", "Emma", "Thompson", "emma.thompson@innovatelab.com", 
                             "InnovateLab", "+1-555-0106", "Innovation and startup sponsor backing creative volunteer initiatives", false),
                
                createSponsor("SportsPro_David", "David", "Martinez", "david.martinez@sportspro.com", 
                             "SportsPro Equipment", "+1-555-0107", "Sports equipment sponsor for youth and community sports programs", true),
                
                createSponsor("ArtsFund_Jennifer", "Jennifer", "Brown", "jennifer.brown@artsfund.org", 
                             "Arts & Culture Fund", "+1-555-0108", "Arts and culture sponsor supporting creative community projects", true),
                
                createSponsor("TechStart_Alex", "Alexander", "Lee", "alex.lee@techstart.io", 
                             "TechStart Incubator", "+1-555-0109", "Technology startup sponsor fostering innovation in volunteering", false),
                
                createSponsor("GlobalReach_Maria", "Maria", "Garcia", "maria.garcia@globalreach.org", 
                             "Global Reach Initiative", "+1-555-0110", "International development sponsor with focus on global volunteer programs", true)
            );
            
            userSponsorRepository.saveAll(sponsors);
            log.info("Successfully initialized {} sponsors", sponsors.size());
        } else {
            log.info("Sponsor data already exists, skipping initialization");
        }
    }
    
    private UserSponsor createSponsor(String username, String firstName, String lastName, 
                                     String email, String companyName, String phoneNumber, 
                                     String bio, boolean isOnline) {
        UserSponsor sponsor = new UserSponsor();
        sponsor.setUsername(username);
        sponsor.setFirstName(firstName);
        sponsor.setLastName(lastName);
        sponsor.setEmail(email);
        sponsor.setCompanyName(companyName);
        sponsor.setPhoneNumber(phoneNumber);
        sponsor.setBio(bio);
        sponsor.setIsActive(true);
        sponsor.setIsOnline(isOnline);
        sponsor.setLastSeen(isOnline ? null : LocalDateTime.now().minusHours((long)(Math.random() * 24)));
        return sponsor;
    }
}
