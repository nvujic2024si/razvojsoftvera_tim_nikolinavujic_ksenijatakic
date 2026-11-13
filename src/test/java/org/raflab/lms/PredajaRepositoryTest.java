package org.raflab.lms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.PredajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PredajaRepositoryTest {
    @Autowired
    PredajaRepository repo;
    @Autowired
    TestEntityManager em;

    Korisnik student;
    StudentskiRad rad;
    Predaja predaja;

    @BeforeEach
    void setUp() {
        student = TestData.student(em, "Ana", "Anic", "RN 23/2024", "aanic@raf.rs");
        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        Provera pr = TestData.provera(em, p, r, TipProvere.KOLOKVIJUM);
        Termin t = TestData.termin(em, pr, "A1");
        Grupa g = TestData.grupa(em, t, "Grupa A");
        rad = TestData.rad(em, g, "Zadatak 1");
        predaja = TestData.predaja(em, student, rad);
        em.flush();
    }

    @Test
    void findByStudentKorisnikIdTest() {
        assertThat(repo.findByStudentKorisnikId(student.getKorisnikId())).hasSize(1);
    }

    @Test
    void findByRadStudentskiRadIdTest() {
        assertThat(repo.findByRadStudentskiRadId(rad.getStudentskiRadId())).hasSize(1);
    }

    @Test
    void findByStudentKorisnikIdAndRadStudentskiRadIdTest() {
        assertThat(repo.findByStudentKorisnikIdAndRadStudentskiRadId(student.getKorisnikId(), rad.getStudentskiRadId())).isPresent();
    }
}