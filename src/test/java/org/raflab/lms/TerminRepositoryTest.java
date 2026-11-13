package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TerminRepositoryTest {
    @Autowired
    TerminRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void findByProveraProveraIdTest() {
        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        Provera pr = TestData.provera(em, p, r, TipProvere.KOLOKVIJUM);
        TestData.termin(em, pr, "A1");
        em.flush();

        assertThat(repo.findByProveraProveraId(pr.getProveraId())).hasSize(1);
    }
}