package org.raflab.lms.repository;

import org.raflab.lms.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Long> {
    List<Termin> findByProveraProveraId(Long proveraId);
}
