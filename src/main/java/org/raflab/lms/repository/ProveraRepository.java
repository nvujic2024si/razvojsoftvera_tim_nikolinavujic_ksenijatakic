package org.raflab.lms.repository;

import org.raflab.lms.model.Provera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveraRepository extends JpaRepository<Provera, Long> {
    List<Provera> findByPredmetPredmetId(Long predmetId);

    List<Provera> findByRokRokId(Long rokId);
}
