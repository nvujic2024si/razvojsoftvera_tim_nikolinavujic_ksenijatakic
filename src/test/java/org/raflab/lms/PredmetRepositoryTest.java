package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.Predmet;
import org.raflab.lms.repository.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PredmetRepositoryTest {
    @Autowired
    PredmetRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void findByKodPredmetaTest() {
        Predmet p = TestData.predmet(em, "Uvod u programiranje", "UUP");
        em.flush();

        assertThat(repo.findByKodPredmeta("UUP")).isPresent().contains(p);
        assertThat(repo.findByKodPredmeta("OOP")).isEmpty();
    }
}