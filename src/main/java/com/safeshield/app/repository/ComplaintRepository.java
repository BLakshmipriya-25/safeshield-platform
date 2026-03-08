package com.safeshield.app.repository;

import com.safeshield.app.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Find complaint by case number
    Complaint findByCaseNumber(String caseNumber);

    // IP address abuse detection
    List<Complaint> findByIpAddressAndDateAfter(String ipAddress, LocalDateTime time);

    // Get complaints of a victim
    List<Complaint> findByVictimId(String victimId);

    // Status statistics
    long countByStatus(String status);

    // ===============================
    // NEW FEATURE
    // Duplicate Complaint Detection
    // ===============================
    List<Complaint> findByVictimIdAndDescription(String victimId, String description);

}