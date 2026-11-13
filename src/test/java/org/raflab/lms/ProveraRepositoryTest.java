package org.raflab.lms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.ProveraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProveraRepositoryTest {
    @Autowired
    ProveraRepository repo;
    @Autowired
    TestEntityManager em;

    Predmet predmet1, predmet2;
    Rok rok;

    @BeforeEach
    void setUp() {
        predmet1 = TestData.predmet(em, "UUP", "UUP");
        predmet2 = TestData.predmet(em, "UUBP", "UUBP");
        rok = TestData.rok(em, "Jun", 2025);
        TestData.provera(em, predmet1, rok, TipProvere.ISPIT);
        em.flush();
    }

    @Test
    void findByPredmetPredmetIdTest() {
        assertThat(repo.findByPredmetPredmetId(predmet1.getPredmetId())).hasSize(1);
        assertThat(repo.findByPredmetPredmetId(predmet2.getPredmetId())).isEmpty();
    }

    @Test
    void findByRokRokIdTest() {
        assertThat(repo.findByRokRokId(rok.getRokId())).hasSize(1);
    }
}