package org.raflab.lms.repository;

import org.raflab.lms.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    Optional<Korisnik> findByBrojIndeksa(String brojIndeksa);

    Optional<Korisnik> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByBrojIndeksa(String brojIndeksa);
}
