package org.raflab.lms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.KomentarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class KomentarRepositoryTest {
    @Autowired
    KomentarRepository repo;
    @Autowired
    TestEntityManager em;

    Korisnik nastavnik;
    Korisnik student;
    Predaja predaja;

    @BeforeEach
    void setUp() {
        student = TestData.student(em, "Ana", "Anic", "RN 23/2024", "aanic@raf.rs");
        nastavnik = TestData.nastavnik(em, "Marko", "Markovic", "mmarkovic@raf.rs");

        Predmet p = TestData.predmet(em, "UUP", "UUP");
        Rok r = TestData.rok(em, "Jun", 2025);
        Provera pr = TestData.provera(em, p, r, TipProvere.KOLOKVIJUM);
        Termin t = TestData.termin(em, pr, "A1");
        Grupa g = TestData.grupa(em, t, "Grupa A");
        StudentskiRad rad = TestData.rad(em, g, "Zadatak 1");

        predaja = TestData.predaja(em, student, rad);
        TestData.komentar(em, predaja, nastavnik);
        em.flush();
    }

    @Test
    void findByPredajaPredajaIdTest() {
        assertThat(repo.findByPredajaPredajaId(predaja.getPredajaId())).hasSize(1);
    }

    @Test
    void findByNastavnikKorisnikIdTest() {
        assertThat(repo.findByNastavnikKorisnikId(nastavnik.getKorisnikId())).hasSize(1);
    }

    @Test
    void findByPredajaStudentKorisnikIdTest() {
        assertThat(repo.findByPredajaStudentKorisnikId(student.getKorisnikId())).hasSize(1);
        assertThat(repo.findByPredajaStudentKorisnikId(nastavnik.getKorisnikId())).isEmpty();
    }

    @Test
    void findByPredajaPredajaIdAndPredajaStudentKorisnikIdTest() {
        assertThat(repo.findByPredajaPredajaIdAndPredajaStudentKorisnikId(predaja.getPredajaId(), student.getKorisnikId())).hasSize(1);
    }
}