package org.raflab.lms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raflab.lms.model.Korisnik;
import org.raflab.lms.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class KorisnikRepositoryTest {
    @Autowired
    KorisnikRepository repo;
    @Autowired
    TestEntityManager em;

    Korisnik student;
    Korisnik nastavnik;

    @BeforeEach
    void setUp() {
        student = TestData.student(em, "Ana", "Anic", "RN 23/2024", "aanic232024rn@raf.rs");
        nastavnik = TestData.nastavnik(em, "Marko", "Markovic", "mmarkovic@raf.rs");
        em.flush();
    }

    @Test
    void findByEmailIgnoreCaseTest() {
        assertThat(repo.findByEmailIgnoreCase("AANIC232024RN@RAF.RS")).isPresent().contains(student);
        assertThat(repo.findByEmailIgnoreCase("mmarkovic@raf.rs")).isPresent().contains(nastavnik);
        assertThat(repo.findByEmailIgnoreCase("nema@raf.rs")).isEmpty();
    }

    @Test
    void findByBrojIndeksaTest() {
        assertThat(repo.findByBrojIndeksa("RN 23/2024")).isPresent().contains(student);
        assertThat(repo.findByBrojIndeksa("RN 00/2024")).isEmpty();
    }

    @Test
    void existsByEmailIgnoreCaseTest() {
        assertThat(repo.existsByEmailIgnoreCase("aanic232024rn@raf.rs")).isTrue();
        assertThat(repo.existsByEmailIgnoreCase("nema@raf.rs")).isFalse();
    }

    @Test
    void existsByBrojIndeksaTest() {
        assertThat(repo.existsByBrojIndeksa("RN 23/2024")).isTrue();
        assertThat(repo.existsByBrojIndeksa("RN 00/2024")).isFalse();
    }
}