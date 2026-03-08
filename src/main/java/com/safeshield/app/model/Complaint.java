package com.safeshield.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String victimId;

    private String type;

    @Column(length = 5000)
    private String description;

    private String evidencePath;

    private String status;

    @Column(unique = true)
    private String caseNumber;



    private LocalDate date = LocalDate.now();

    // 🔥 NEW FIELDS (LIVE LOCATION)
    private Double latitude;
    private Double longitude;
    private String ipAddress;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidencePath() {
        return evidencePath;
    }

    public void setEvidencePath(String evidencePath) {
        this.evidencePath = evidencePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }



    public LocalDate getDate() {
        return date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
