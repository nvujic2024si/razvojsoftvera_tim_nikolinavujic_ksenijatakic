package org.raflab.lms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.PrijavaZaProveruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PrijavaZaProveruRepositoryTest {
    @Autowired
    PrijavaZaProveruRepository repo;
    @Autowired
    TestEntityManager em;

    Korisnik student;
    Provera provera;
    Grupa grupa;

    @BeforeEach
    void setUp() {
        student = TestData.student(em, "Ana", "Anic", "RN 23/2024", "aanic@raf.rs");
        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        provera = TestData.provera(em, p, r, TipProvere.ISPIT);
        Termin t = TestData.termin(em, provera, "A1");
        grupa = TestData.grupa(em, t, "Grupa A");
        TestData.prijava(em, student, provera, grupa);
        em.flush();
    }

    @Test
    void findByStudentKorisnikIdTest() {
        assertThat(repo.findByStudentKorisnikId(student.getKorisnikId())).hasSize(1);
    }

    @Test
    void findByGrupaTerminTerminIdTest() {
        assertThat(repo.findByGrupaTerminTerminId(grupa.getTermin().getTerminId())).hasSize(1);
    }

    @Test
    void findByStudentKorisnikIdAndProveraProveraIdTest() {
        assertThat(repo.findByStudentKorisnikIdAndProveraProveraId(student.getKorisnikId(), provera.getProveraId())).isPresent();
    }
}