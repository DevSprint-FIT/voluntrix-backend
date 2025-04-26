package com.DevSprint.voluntrix_backend.model;

import jakarta.persistence.*;

@Entity
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sponsorId;

    private String jobTitle;
    private String mobileNumber;
    private boolean isVerified;
    private String company;

    // Constructors
    public Sponsor() {}

    public Sponsor(String jobTitle, String mobileNumber, boolean isVerified, String company) {
        this.jobTitle = jobTitle;
        this.mobileNumber = mobileNumber;
        this.isVerified = isVerified;
        this.company = company;
    }

    // Getters and Setters

    public Long getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Long sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
