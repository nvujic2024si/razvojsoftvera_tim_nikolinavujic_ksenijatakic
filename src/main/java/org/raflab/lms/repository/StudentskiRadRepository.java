package org.raflab.lms.repository;

import org.raflab.lms.model.StudentskiRad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentskiRadRepository extends JpaRepository<StudentskiRad, Long> {
    List<StudentskiRad> findByGrupaGrupaId(Long grupaId);

    Optional<StudentskiRad> findFirstByGrupaGrupaId(Long grupaId);

    boolean existsByGrupaGrupaId(Long grupaId);
}