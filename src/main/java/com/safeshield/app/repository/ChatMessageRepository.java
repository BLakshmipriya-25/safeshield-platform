package com.safeshield.app.repository;

import com.safeshield.app.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Get full conversation
    List<ChatMessage> findByVictimIdOrderByTimestampAsc(String victimId);

    // Get latest counsellor message
    ChatMessage findTop1ByVictimIdAndSenderOrderByTimestampDesc(String victimId, String sender);

    // Get unique victim IDs for counsellor dashboard
    @Query("SELECT DISTINCT m.victimId FROM ChatMessage m")
    List<String> findUniqueVictimIds();

    // ⭐ NEW METHOD (for Admin High Risk Cases)
    long countByPriority(String priority);

}