package com.safeshield.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String victimId;

    private String sender;

    @Column(length = 2000)
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();

    // NORMAL or HIGH
    private String priority = "NORMAL";

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}