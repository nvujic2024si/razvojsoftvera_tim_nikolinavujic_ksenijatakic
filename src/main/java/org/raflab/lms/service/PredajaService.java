package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PredajaFilter;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ForbiddenException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PredajaService {
    private final PredajaRepository predajaRepository;
    private final KorisnikRepository korisnikRepository;
    private final StudentskiRadRepository studentskiRadRepository;
    private final PrijavaZaProveruRepository prijavaZaProveruRepository;
    private final GrupaRepository grupaRepository;
    private final FileStorageService fileStorageService;
    private final DrziPredmetService drziPredmetService;

    /*
    Student unosi termin i grupu (identitet dolazi iz tokena). Server proverava da li je student
    na spisku za tu proveru u bas toj grupi i da li termin traje. Ako je student
    vec preuzeo, dobija istu predaju (npr. kad nastavlja rad na drugom racunaru).
    */
    @Transactional
    public Predaja preuzmi(Long studentId, Long terminId, Long grupaId) {
        Korisnik student = korisnikRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Student: " + studentId));
        Grupa grupa = grupaRepository.findById(grupaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Grupa: " + grupaId));
        Termin termin = grupa.getTermin();

        if (!termin.getTerminId().equals(terminId)) {
            throw new BadRequestException("Grupa ne pripada izabranom terminu.");
        }

        Long proveraId = termin.getProvera().getProveraId();
        PrijavaZaProveru prijava = prijavaZaProveruRepository
                .findByStudentKorisnikIdAndProveraProveraId(studentId, proveraId)
                .orElseThrow(() -> new ForbiddenException("Niste na spisku za polaganje ove provere."));
        if (!prijava.getGrupa().getGrupaId().equals(grupaId)) {
            throw new ForbiddenException("Prijavljeni ste u drugu grupu: " + prijava.getGrupa().getNazivGrupe() + ".");
        }

        proveriDaTerminTraje(termin);

        StudentskiRad rad = studentskiRadRepository.findFirstByGrupaGrupaId(grupaId)
                .orElseThrow(() -> new ResourceNotFoundException("Za ovu grupu nije postavljen zadatak."));

        return predajaRepository
                .findByStudentKorisnikIdAndRadStudentskiRadId(studentId, rad.getStudentskiRadId())
                .orElseGet(() -> {
                    Predaja predaja = new Predaja();
                    predaja.setStudent(student);
                    predaja.setRad(rad);
                    predaja.setVremePreuzimanja(LocalDateTime.now());
                    return predajaRepository.save(predaja);
                });
    }

    // zip sa zadatkom; samo vlasnik predaje i samo dok termin traje
    @Transactional(readOnly = true)
    public Resource zadatakZaPreuzimanje(Long predajaId, Long studentId) {
        Predaja predaja = findZaStudenta(predajaId, studentId);
        proveriDaTerminTraje(predaja.getRad().getGrupa().getTermin());
        return fileStorageService.ucitaj(predaja.getRad().getPutanjaDoFajla());
    }

    // predaja moze vise puta do isteka termina, poslednja prepisuje prethodnu
    @Transactional
    public Predaja predaj(Long predajaId, Long studentId, MultipartFile fajl) {
        Predaja predaja = findZaStudenta(predajaId, studentId);

        Termin termin = predaja.getRad().getGrupa().getTermin();
        LocalDateTime sada = LocalDateTime.now();
        if (sada.isAfter(termin.getVremeZavrsetka())) {
            throw new BadRequestException("Termin je istekao, predaja nije moguca.");
        }

        predaja.setPutanjaDoPredaje(fileStorageService.sacuvajPredaju(predajaId, fajl));
        predaja.setVremePredaje(sada);
        return predajaRepository.save(predaja);
    }

    public List<Predaja> findByStudent(Long korisnikId) {
        return predajaRepository.findByStudentKorisnikId(korisnikId);
    }

    // nastavnik (svuda se proverava da drzi predmet)

    @Transactional
    public Predaja dodeliPoene(Long predajaId, Integer poeni) {
        if (poeni == null || poeni < 0) {
            throw new BadRequestException("Poeni moraju biti nenegativan broj.");
        }
        Predaja predaja = findZaNastavnika(predajaId);
        predaja.setPoeni(poeni);
        return predajaRepository.save(predaja);
    }

    public Predaja findZaNastavnika(Long predajaId) {
        Predaja predaja = findById(predajaId);
        drziPredmetService.proveriPristup(predaja);
        return predaja;
    }

    public List<Predaja> findByRad(Long radId) {
        StudentskiRad rad = studentskiRadRepository.findById(radId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji StudentskiRad sa id: " + radId));
        drziPredmetService.proveriPristup(rad.getGrupa());
        return predajaRepository.findByRadStudentskiRadId(radId);
    }

    @Transactional(readOnly = true)
    public Resource resenjeZaNastavnika(Long predajaId) {
        Predaja predaja = findZaNastavnika(predajaId);
        if (predaja.getVremePredaje() == null || predaja.getPutanjaDoPredaje() == null) {
            throw new ResourceNotFoundException("Student jos nije predao resenje.");
        }
        return fileStorageService.ucitaj(predaja.getPutanjaDoPredaje());
    }

    // filteri po predmetu, roku, terminu, grupi, ucionici, studentu; uvek samo predmeti koje nastavnik drzi
    public List<Predaja> pretrazi(PredajaFilter filter) {
        if (filter.getPredmetId() != null) {
            drziPredmetService.proveriPristup(filter.getPredmetId());
        }
        List<Long> dozvoljeni = drziPredmetService.idPredmetaUlogovanog();
        if (dozvoljeni.isEmpty()) {
            return List.of();
        }
        return predajaRepository.findAll(PredajaSpecifications.filter(filter, dozvoljeni));
    }

    // jedan zip sa resenjima svih studenata koji odgovaraju kriterijumima (unutra je zip svakog studenta)
    @Transactional(readOnly = true)
    public byte[] spakujResenja(PredajaFilter filter) {
        List<Predaja> predate = pretrazi(filter).stream()
                .filter(p -> p.getVremePredaje() != null && p.getPutanjaDoPredaje() != null)
                .toList();
        if (predate.isEmpty()) {
            throw new ResourceNotFoundException("Nema predatih resenja za zadate kriterijume.");
        }

        Map<String, String> fajlovi = new LinkedHashMap<>();
        for (Predaja p : predate) {
            String indeks = p.getStudent().getBrojIndeksa() != null
                    ? p.getStudent().getBrojIndeksa() : "student" + p.getStudent().getKorisnikId();
            String ime = indeks.replaceAll("[^A-Za-z0-9._-]", "_") + "_" + p.getPredajaId() + ".zip";
            fajlovi.put(ime, p.getPutanjaDoPredaje());
        }
        return fileStorageService.spakuj(fajlovi);
    }

    public Predaja findById(Long id) {
        return predajaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Predaja: " + id));
    }

    private Predaja findZaStudenta(Long predajaId, Long studentId) {
        Predaja predaja = findById(predajaId);
        if (!predaja.getStudent().getKorisnikId().equals(studentId)) {
            throw new ForbiddenException("Predaja ne pripada ulogovanom studentu.");
        }
        return predaja;
    }

    // granice termina su ukljucene
    private void proveriDaTerminTraje(Termin termin) {
        LocalDateTime sada = LocalDateTime.now();
        if (sada.isBefore(termin.getVremePocetka()) || sada.isAfter(termin.getVremeZavrsetka())) {
            throw new BadRequestException("Zadatak nije dostupan van termina.");
        }
    }
}