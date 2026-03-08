package com.safeshield.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EvidenceAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String officerName;
    private String caseNumber;
    private LocalDateTime accessedAt;

    // getters & setters
}