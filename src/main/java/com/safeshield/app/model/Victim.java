package com.safeshield.app.model;

import jakarta.persistence.*;

@Entity
public class Victim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String victimId;

    private String recoveryCode;

    // NEW FIELD
    private boolean blocked = false;

    public Long getId() {
        return id;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    // NEW METHODS
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}