package org.raflab.lms;

import lombok.RequiredArgsConstructor;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// podaci za razvoj; radi samo u dev profilu, ne koristiti u produkciji

@Profile("dev")
@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

    private final KorisnikRepository korisnikRepository;
    private final PredmetRepository predmetRepository;
    private final RokRepository rokRepository;
    private final ProveraRepository proveraRepository;
    private final TerminRepository terminRepository;
    private final GrupaRepository grupaRepository;
    private final StudentskiRadRepository studentskiRadRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final PrijavaZaProveruRepository prijavaZaProveruRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (korisnikRepository.count() > 0) return;

        Korisnik nastavnik = new Korisnik();
        nastavnik.setIme("Marko");
        nastavnik.setPrezime("Markovic");
        nastavnik.setEmail("mmarkovic@raf.rs");
        nastavnik.setLozinka(passwordEncoder.encode("lozinka123"));
        nastavnik.setUloga(Uloga.NASTAVNIK);
        korisnikRepository.save(nastavnik);

        Korisnik student = new Korisnik();
        student.setIme("Ana");
        student.setPrezime("Anic");
        student.setBrojIndeksa("RN 23/2024");
        student.setEmail("aanic232024rn@raf.rs");
        student.setLozinka(passwordEncoder.encode("lozinka123"));
        student.setUloga(Uloga.STUDENT);
        korisnikRepository.save(student);

        String[][] podaciOPredmetima = {
                {"UUP", "Uvod u programiranje"},
                {"UUBP", "Uvod u baze podataka"},
                {"RSW", "Razvoj softvera"},
                {"DSW", "Dizajn softvera"},
                {"UUSI", "Uvod u softversko inženjerstvo"}
        };

        List<Predmet> predmeti = new ArrayList<>();
        for (String[] p : podaciOPredmetima) {
            Predmet predmet = new Predmet();
            predmet.setKodPredmeta(p[0]);
            predmet.setNazivPredmeta(p[1]);
            predmeti.add(predmetRepository.save(predmet));
        }

        for (Predmet p : predmeti) {
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnik);
            dp.setPredmet(p);
            drziPredmetRepository.save(dp);
        }

        Predmet uup = predmeti.get(0);

        Rok rok = new Rok();
        rok.setNazivRoka("Prvi kolokvijum");
        rok.setGodina(2025);
        rokRepository.save(rok);

        Provera provera = new Provera();
        provera.setTipProvere(TipProvere.KOLOKVIJUM);
        provera.setPredmet(uup);
        provera.setRok(rok);
        proveraRepository.save(provera);

        Termin termin = new Termin();
        termin.setUcionica("RAF5");
        termin.setVremePocetka(LocalDateTime.now().minusHours(1));
        termin.setVremeZavrsetka(LocalDateTime.now().plusHours(2));
        termin.setProvera(provera);
        terminRepository.save(termin);

        Grupa grupa = new Grupa();
        grupa.setNazivGrupe("Grupa A");
        grupa.setTermin(termin);
        grupaRepository.save(grupa);

        StudentskiRad rad = new StudentskiRad();
        rad.setNazivZadatka("Kolokvijumski zadatak - UUP");
        rad.setGrupa(grupa);
        rad = studentskiRadRepository.save(rad);

        String relativnaPutanja = "zadaci/" + rad.getStudentskiRadId() + "/projekat.zip";
        napraviTestniZipFajl(relativnaPutanja);
        rad.setPutanjaDoFajla(relativnaPutanja);
        studentskiRadRepository.save(rad);

        PrijavaZaProveru prijava = new PrijavaZaProveru();
        prijava.setStudent(student);
        prijava.setProvera(provera);
        prijava.setGrupa(grupa);
        prijavaZaProveruRepository.save(prijava);
    }

    // Pomocna metoda koja pravi mali .zip fajl u storage folderu
    private void napraviTestniZipFajl(String relativnaPutanja) {
        try {
            Path putanja = Paths.get("./storage").resolve(relativnaPutanja);
            Files.createDirectories(putanja.getParent());

            try (OutputStream fos = Files.newOutputStream(putanja);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {

                ZipEntry zipEntry = new ZipEntry("Main.java");
                zipOut.putNextEntry(zipEntry);
                String kod = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello UUP!\");\n    }\n}";
                zipOut.write(kod.getBytes());
                zipOut.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException("Nije uspelo pravljenje testnog zip fajla", e);
        }
    }
}