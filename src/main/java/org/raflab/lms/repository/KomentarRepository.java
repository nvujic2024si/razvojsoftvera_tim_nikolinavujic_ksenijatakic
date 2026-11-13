package org.raflab.lms.repository;

import org.raflab.lms.model.Komentar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KomentarRepository extends JpaRepository<Komentar, Long> {
    List<Komentar> findByPredajaPredajaId(Long predajaId);

    List<Komentar> findByNastavnikKorisnikId(Long nastavnikId);

    List<Komentar> findByPredajaStudentKorisnikId(Long studentId);

    List<Komentar> findByPredajaPredajaIdAndPredajaStudentKorisnikId(Long predajaId, Long studentId);
}