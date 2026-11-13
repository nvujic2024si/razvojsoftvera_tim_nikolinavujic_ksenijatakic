package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PrijavaZaProveruRequest;
import org.raflab.lms.controllers.request.SpisakRequest;
import org.raflab.lms.controllers.response.SpisakResponse;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ConflictException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.GrupaRepository;
import org.raflab.lms.repository.KorisnikRepository;
import org.raflab.lms.repository.PrijavaZaProveruRepository;
import org.raflab.lms.repository.ProveraRepository;
import org.raflab.lms.utils.IndeksUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PrijavaZaProveruService {
    private final PrijavaZaProveruRepository prijavaZaProveruRepository;
    private final KorisnikRepository studentRepository;
    private final ProveraRepository proveraRepository;
    private final GrupaRepository grupaRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public PrijavaZaProveru save(PrijavaZaProveruRequest request) {
        Korisnik student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Student: " + request.getStudentId()));
        Provera provera = proveraRepository.findById(request.getProveraId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Provera: " + request.getProveraId()));
        Grupa grupa = grupaRepository.findById(request.getGrupaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Grupa: " + request.getGrupaId()));

        drziPredmetService.proveriPristup(grupa);

        if (!grupa.getTermin().getProvera().getProveraId().equals(provera.getProveraId())) {
            throw new BadRequestException("Grupa ne pripada izabranoj proveri.");
        }
        if (student.getUloga() != Uloga.STUDENT) {
            throw new BadRequestException("Korisnik nije student.");
        }
        if (prijavaZaProveruRepository
                .findByStudentKorisnikIdAndProveraProveraId(student.getKorisnikId(), provera.getProveraId())
                .isPresent()) {
            throw new ConflictException("Student je vec na spisku za ovu proveru.");
        }

        PrijavaZaProveru prijava = new PrijavaZaProveru();
        prijava.setStudent(student);
        prijava.setProvera(provera);
        prijava.setGrupa(grupa);
        return prijavaZaProveruRepository.save(prijava);
    }

    // nastavnik unosi spisak studenata (po broju indeksa) za jednu grupu; provera se uzima iz grupe
    @Transactional
    public SpisakResponse unesiSpisak(SpisakRequest request) {
        Grupa grupa = grupaRepository.findById(request.getGrupaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Grupa: " + request.getGrupaId()));
        drziPredmetService.proveriPristup(grupa);
        Provera provera = grupa.getTermin().getProvera();

        int dodato = 0;
        int vecPostojalo = 0;
        List<String> uDrugojGrupi = new ArrayList<>();
        List<String> nepoznati = new ArrayList<>();

        LinkedHashSet<String> indeksi = new LinkedHashSet<>();
        for (String i : request.getBrojeviIndeksa()) {
            String n = IndeksUtil.normalizuj(i);
            if (n != null) {
                indeksi.add(n);
            }
        }

        for (String indeks : indeksi) {
            Optional<Korisnik> student = studentRepository.findByBrojIndeksa(indeks)
                    .filter(k -> k.getUloga() == Uloga.STUDENT);
            if (student.isEmpty()) {
                nepoznati.add(indeks);
                continue;
            }

            Optional<PrijavaZaProveru> postojeca = prijavaZaProveruRepository
                    .findByStudentKorisnikIdAndProveraProveraId(student.get().getKorisnikId(), provera.getProveraId());
            if (postojeca.isPresent()) {
                if (postojeca.get().getGrupa().getGrupaId().equals(grupa.getGrupaId())) {
                    vecPostojalo++;
                } else {
                    uDrugojGrupi.add(indeks);
                }
                continue;
            }

            PrijavaZaProveru prijava = new PrijavaZaProveru();
            prijava.setStudent(student.get());
            prijava.setProvera(provera);
            prijava.setGrupa(grupa);
            prijavaZaProveruRepository.save(prijava);
            dodato++;
        }
        return new SpisakResponse(dodato, vecPostojalo, uDrugojGrupi, nepoznati);
    }

    public List<PrijavaZaProveru> findAll() {
        return prijavaZaProveruRepository.findAll();
    }

    public PrijavaZaProveru findById(Long id) {
        return prijavaZaProveruRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji PrijavaZaProveru sa id: " + id));
    }

    public List<PrijavaZaProveru> findByStudent(Long korisnikId) {
        return prijavaZaProveruRepository.findByStudentKorisnikId(korisnikId);
    }

    public List<PrijavaZaProveru> findByGrupa(Long grupaId) {
        return prijavaZaProveruRepository.findByGrupaGrupaId(grupaId);
    }
}