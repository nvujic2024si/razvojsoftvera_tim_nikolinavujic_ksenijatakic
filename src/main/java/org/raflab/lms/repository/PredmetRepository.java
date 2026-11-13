package org.raflab.lms.repository;

import org.raflab.lms.model.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PredmetRepository extends JpaRepository<Predmet, Long> {
    Optional<Predmet> findByKodPredmeta(String kodPredmeta);
}
