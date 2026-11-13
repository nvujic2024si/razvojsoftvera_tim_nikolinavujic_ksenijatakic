package org.raflab.lms;

import org.raflab.lms.model.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

class TestData {

    static Korisnik student(TestEntityManager em, String ime, String prezime, String brojIndeksa, String email) {
        Korisnik k = new Korisnik();
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setBrojIndeksa(brojIndeksa);
        k.setEmail(email);
        k.setLozinka("lozinka123");
        k.setUloga(Uloga.STUDENT);
        return em.persist(k);
    }

    static Korisnik nastavnik(TestEntityManager em, String ime, String prezime, String email) {
        Korisnik k = new Korisnik();
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setEmail(email);
        k.setLozinka("lozinka123");
        k.setUloga(Uloga.NASTAVNIK);
        return em.persist(k);
    }

    static Predmet predmet(TestEntityManager em, String naziv, String kod) {
        Predmet p = new Predmet();
        p.setNazivPredmeta(naziv);
        p.setKodPredmeta(kod);
        return em.persist(p);
    }

    static Rok rok(TestEntityManager em, String naziv, int godina) {
        Rok r = new Rok();
        r.setNazivRoka(naziv);
        r.setGodina(godina);
        return em.persist(r);
    }

    static Provera provera(TestEntityManager em, Predmet predmet, Rok rok, TipProvere tip) {
        Provera p = new Provera();
        p.setPredmet(predmet);
        p.setRok(rok);
        p.setTipProvere(tip);
        return em.persist(p);
    }

    static Termin termin(TestEntityManager em, Provera provera, String ucionica) {
        Termin t = new Termin();
        t.setUcionica(ucionica);
        t.setVremePocetka(LocalDateTime.now().minusHours(1));
        t.setVremeZavrsetka(LocalDateTime.now().plusHours(2));
        t.setProvera(provera);
        return em.persist(t);
    }

    static Grupa grupa(TestEntityManager em, Termin termin, String naziv) {
        Grupa g = new Grupa();
        g.setTermin(termin);
        g.setNazivGrupe(naziv);
        return em.persist(g);
    }

    static StudentskiRad rad(TestEntityManager em, Grupa grupa, String naziv) {
        StudentskiRad r = new StudentskiRad();
        r.setGrupa(grupa);
        r.setNazivZadatka(naziv);
        r.setPutanjaDoFajla("storage/zadaci/test.zip");
        return em.persist(r);
    }

    static DrziPredmet drziPredmet(TestEntityManager em, Korisnik nastavnik, Predmet predmet) {
        DrziPredmet d = new DrziPredmet();
        d.setNastavnik(nastavnik);
        d.setPredmet(predmet);
        return em.persist(d);
    }

    static PrijavaZaProveru prijava(TestEntityManager em, Korisnik student, Provera provera, Grupa grupa) {
        PrijavaZaProveru p = new PrijavaZaProveru();
        p.setStudent(student);
        p.setProvera(provera);
        p.setGrupa(grupa);
        return em.persist(p);
    }

    static Predaja predaja(TestEntityManager em, Korisnik student, StudentskiRad rad) {
        Predaja p = new Predaja();
        p.setStudent(student);
        p.setRad(rad);
        p.setVremePreuzimanja(LocalDateTime.now());
        return em.persist(p);
    }

    static Komentar komentar(TestEntityManager em, Predaja predaja, Korisnik nastavnik) {
        Komentar k = new Komentar();
        k.setPredaja(predaja);
        k.setNastavnik(nastavnik);
        k.setNazivFajla("Main.java");
        k.setLinijaOd(10);
        k.setLinijaDo(15);
        k.setTekst("Test komentar");
        return em.persist(k);
    }
}