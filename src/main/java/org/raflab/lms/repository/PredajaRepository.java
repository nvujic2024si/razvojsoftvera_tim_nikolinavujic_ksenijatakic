package org.raflab.lms.repository;

import org.raflab.lms.model.Predaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PredajaRepository extends JpaRepository<Predaja, Long>, JpaSpecificationExecutor<Predaja> {
    List<Predaja> findByStudentKorisnikId(Long korisnikId);

    List<Predaja> findByRadStudentskiRadId(Long studentskiRadId);

    Optional<Predaja> findByStudentKorisnikIdAndRadStudentskiRadId(Long korisnikId, Long studentskiRadId);

    List<Predaja> findByRadGrupaTerminTerminId(Long terminId);
}