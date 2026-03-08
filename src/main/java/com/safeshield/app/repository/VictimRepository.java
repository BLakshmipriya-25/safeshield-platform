package com.safeshield.app.repository;

import com.safeshield.app.model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VictimRepository extends JpaRepository<Victim, Long> {

    // find by victimId
    Optional<Victim> findByVictimId(String victimId);

    // 🔹 NEW METHOD (for login validation)
    Optional<Victim> findByVictimIdAndRecoveryCode(String victimId, String recoveryCode);

}