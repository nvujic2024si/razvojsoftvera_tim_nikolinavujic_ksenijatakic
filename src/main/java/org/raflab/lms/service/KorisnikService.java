package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.KorisnikRequest;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ConflictException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Korisnik;
import org.raflab.lms.model.Uloga;
import org.raflab.lms.repository.KorisnikRepository;
import org.raflab.lms.utils.Converters;
import org.raflab.lms.utils.IndeksUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class KorisnikService {
    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Korisnik save(KorisnikRequest request) {
        if (korisnikRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Email vec postoji: " + request.getEmail());
        }

        // indeks se cuva normalizovan da ga spisak nadje bez obzira na razmake i velika slova
        String indeks = IndeksUtil.normalizuj(request.getBrojIndeksa());

        if (request.getUloga() == Uloga.STUDENT && indeks == null) {
            throw new BadRequestException("Student mora imati broj indeksa.");
        }
        if (indeks != null && korisnikRepository.existsByBrojIndeksa(indeks)) {
            throw new ConflictException("Broj indeksa vec postoji: " + indeks);
        }

        Korisnik korisnik = Converters.toKorisnik(request);
        korisnik.setBrojIndeksa(indeks);
        // lozinka je opciona; korisnik bez lozinke ne moze da se prijavi lozinkom
        if (request.getLozinka() != null && !request.getLozinka().isBlank()) {
            korisnik.setLozinka(passwordEncoder.encode(request.getLozinka()));
        }
        return korisnikRepository.save(korisnik);
    }

    public List<Korisnik> findAll() {
        return korisnikRepository.findAll();
    }

    public Korisnik findById(Long id) {
        return korisnikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Korisnik sa id: " + id));
    }
}
