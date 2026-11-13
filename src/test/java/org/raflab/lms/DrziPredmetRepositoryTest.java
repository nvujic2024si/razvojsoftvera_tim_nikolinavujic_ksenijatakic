package org.raflab.lms;

import org.junit.jupiter.api.Test;
import org.raflab.lms.model.Korisnik;
import org.raflab.lms.model.Predmet;
import org.raflab.lms.repository.DrziPredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DrziPredmetRepositoryTest {
    @Autowired
    DrziPredmetRepository repo;
    @Autowired
    TestEntityManager em;

    @Test
    void pronalaziPredmeteZaNastavnikaTest() {
        Korisnik nastavnik = TestData.nastavnik(em, "Marko", "Markovic", "mmarkovic@raf.rs");
        Predmet predmet = TestData.predmet(em, "UUP", "UUP");
        TestData.drziPredmet(em, nastavnik, predmet);
        em.flush();

        assertThat(repo.findByNastavnikKorisnikId(nastavnik.getKorisnikId())).hasSize(1);
        assertThat(repo.existsByNastavnikKorisnikIdAndPredmetPredmetId(nastavnik.getKorisnikId(), predmet.getPredmetId())).isTrue();
        assertThat(repo.existsByNastavnikKorisnikIdAndPredmetPredmetId(999L, predmet.getPredmetId())).isFalse();
    }
}