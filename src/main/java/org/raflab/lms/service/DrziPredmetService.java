package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.DrziPredmetRequest;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ConflictException;
import org.raflab.lms.exceptions.ForbiddenException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.DrziPredmetRepository;
import org.raflab.lms.repository.KorisnikRepository;
import org.raflab.lms.repository.PredmetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class DrziPredmetService {
    private final DrziPredmetRepository drziPredmetRepository;
    private final KorisnikRepository nastavnikRepository;
    private final PredmetRepository predmetRepository;
    private final PristupService pristupService;

    @Transactional
    public DrziPredmet save(DrziPredmetRequest request) {
        Korisnik nastavnik = nastavnikRepository.findById(request.getNastavnikId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Nastavnik: " + request.getNastavnikId()));
        if (nastavnik.getUloga() != Uloga.NASTAVNIK) {
            throw new BadRequestException("Korisnik nije nastavnik.");
        }
        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Predmet: " + request.getPredmetId()));
        if (drziPredmetRepository.existsByNastavnikKorisnikIdAndPredmetPredmetId(
                nastavnik.getKorisnikId(), predmet.getPredmetId())) {
            throw new ConflictException("Nastavnik vec drzi ovaj predmet.");
        }

        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setNastavnik(nastavnik);
        drziPredmet.setPredmet(predmet);
        return drziPredmetRepository.save(drziPredmet);
    }

    public List<DrziPredmet> findAll() {
        return drziPredmetRepository.findAll();
    }

    public DrziPredmet findById(Long id) {
        return drziPredmetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji DrziPredmet sa id: " + id));
    }

    public List<DrziPredmet> findByNastavnik(Long nastavnikId) {
        return drziPredmetRepository.findByNastavnikKorisnikId(nastavnikId);
    }

    // predmeti ulogovanog nastavnika
    public List<DrziPredmet> mojiPredmeti() {
        return findByNastavnik(pristupService.ulogovaniKorisnikId());
    }

    public List<Long> idPredmetaUlogovanog() {
        return mojiPredmeti().stream().map(d -> d.getPredmet().getPredmetId()).toList();
    }

    // provera pristupa: baca 403 ako ulogovani nastavnik ne drzi predmet
    public void proveriPristup(Long predmetId) {
        Long nastavnikId = pristupService.ulogovaniKorisnikId();
        if (nastavnikId == null
                || !drziPredmetRepository.existsByNastavnikKorisnikIdAndPredmetPredmetId(nastavnikId, predmetId)) {
            throw new ForbiddenException("Ne drzite ovaj predmet.");
        }
    }

    public void proveriPristup(Provera provera) {
        proveriPristup(provera.getPredmet().getPredmetId());
    }

    public void proveriPristup(Termin termin) {
        proveriPristup(termin.getProvera());
    }

    public void proveriPristup(Grupa grupa) {
        proveriPristup(grupa.getTermin());
    }

    public void proveriPristup(Predaja predaja) {
        proveriPristup(predaja.getRad().getGrupa());
    }
}