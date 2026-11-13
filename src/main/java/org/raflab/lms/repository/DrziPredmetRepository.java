package org.raflab.lms.repository;

import org.raflab.lms.model.DrziPredmet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrziPredmetRepository extends JpaRepository<DrziPredmet, Long> {
    List<DrziPredmet> findByNastavnikKorisnikId(Long nastavnikId);

    boolean existsByNastavnikKorisnikIdAndPredmetPredmetId(Long nastavnikId, Long predmetId);
}
