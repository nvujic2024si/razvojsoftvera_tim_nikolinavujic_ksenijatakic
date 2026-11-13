package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.StudentskiRadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentskiRadRepositoryTest {
    @Autowired
    StudentskiRadRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void findFirstByGrupaGrupaIdTest() {
        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        Provera pr = TestData.provera(em, p, r, TipProvere.KOLOKVIJUM);
        Termin t = TestData.termin(em, pr, "A1");
        Grupa g = TestData.grupa(em, t, "Grupa A");
        TestData.rad(em, g, "Zadatak 1");
        em.flush();

        assertThat(repo.findFirstByGrupaGrupaId(g.getGrupaId())).isPresent();
        assertThat(repo.existsByGrupaGrupaId(g.getGrupaId())).isTrue();
    }
}