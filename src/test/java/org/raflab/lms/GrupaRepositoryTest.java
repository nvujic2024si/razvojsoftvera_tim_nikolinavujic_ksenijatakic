package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.GrupaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GrupaRepositoryTest {
    @Autowired
    GrupaRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void findByTerminTerminIdTest() {
        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        Provera pr = TestData.provera(em, p, r, TipProvere.KOLOKVIJUM);
        Termin t = TestData.termin(em, pr, "A1");
        TestData.grupa(em, t, "Grupa A");
        em.flush();

        assertThat(repo.findByTerminTerminId(t.getTerminId())).hasSize(1);
    }
}