package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.Rok;
import org.raflab.lms.repository.RokRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RokRepositoryTest {
    @Autowired
    RokRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void saveAndFindTest() {
        Rok r = TestData.rok(em, "Jun 2025", 2025);
        em.flush();

        assertThat(repo.findById(r.getRokId())).isPresent();
        assertThat(repo.findAll()).hasSize(1);
    }
}