package org.raflab.lms.repository;

import org.raflab.lms.model.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupaRepository extends JpaRepository<Grupa, Long> {
    List<Grupa> findByTerminTerminId(Long terminId);
}
