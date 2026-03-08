package com.safeshield.app.service;

import com.safeshield.app.model.Complaint;
import com.safeshield.app.model.Victim;
import com.safeshield.app.repository.ComplaintRepository;
import com.safeshield.app.repository.VictimRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VictimService {

    private final VictimRepository victimRepository;
    private final ComplaintRepository complaintRepository;

    public VictimService(VictimRepository victimRepository,
                         ComplaintRepository complaintRepository) {
        this.victimRepository = victimRepository;
        this.complaintRepository = complaintRepository;
    }

    // ===============================
    // 1️⃣ Create Anonymous Victim Account
    // ===============================
    public Victim createVictimAccount() {

        String victimId = "VIC" + UUID.randomUUID().toString().substring(0, 6);
        String recoveryCode = UUID.randomUUID().toString().substring(0, 8);

        Victim victim = new Victim();
        victim.setVictimId(victimId);
        victim.setRecoveryCode(recoveryCode);

        return victimRepository.save(victim);
    }

    // ===============================
    // 2️⃣ Validate Login
    // ===============================
    public boolean validateLogin(String victimId, String recoveryCode) {

        Optional<Victim> victim = victimRepository.findByVictimId(victimId);

        return victim.isPresent() &&
                victim.get().getRecoveryCode().equals(recoveryCode);
    }

    // ===============================
    // 3️⃣ File Complaint (LIVE BROWSER LOCATION VERSION)
    // ===============================
    public void fileComplaint(String victimId,
                              String type,
                              String description,
                              String filePath,
                              Double latitude,
                              Double longitude) {

        Complaint complaint = new Complaint();

        complaint.setVictimId(victimId);
        complaint.setType(type);
        complaint.setDescription(description);
        complaint.setEvidencePath(filePath);

        complaint.setStatus("Under Review");
        complaint.setCaseNumber("CASE" + System.currentTimeMillis());

        // 🔥 LIVE LOCATION FROM BROWSER
        complaint.setLatitude(latitude);
        complaint.setLongitude(longitude);

        // 🔥 Simulated IP (for demo realism)
        complaint.setIpAddress("103.12.45.78");

        complaintRepository.save(complaint);
    }

    // ===============================
    // 4️⃣ Get Victim Complaints
    // ===============================
    public List<Complaint> getVictimComplaints(String victimId) {
        return complaintRepository.findByVictimId(victimId);
    }

    // ===============================
    // 5️⃣ Get Complaint By ID
    // ===============================
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }
}