package org.raflab.lms.repository;

import org.raflab.lms.model.PrijavaZaProveru;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrijavaZaProveruRepository extends JpaRepository<PrijavaZaProveru, Long> {
    List<PrijavaZaProveru> findByStudentKorisnikId(Long studentId);

    List<PrijavaZaProveru> findByGrupaGrupaId(Long grupaId);

    List<PrijavaZaProveru> findByGrupaTerminTerminId(Long terminId);

    Optional<PrijavaZaProveru> findByStudentKorisnikIdAndProveraProveraId(Long studentId, Long proveraId);
}