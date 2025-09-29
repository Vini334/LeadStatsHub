package com.vini334.dashboard.repository;

import com.vini334.dashboard.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    long countByType(String type);
}
