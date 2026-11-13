package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.KomentarRequest;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.*;
import org.raflab.lms.repository.KomentarRepository;
import org.raflab.lms.repository.KorisnikRepository;
import org.raflab.lms.repository.PredajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class KomentarService {
    private final KomentarRepository komentarRepository;
    private final PredajaRepository predajaRepository;
    private final KorisnikRepository nastavnikRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public Komentar save(KomentarRequest request, Long nastavnikId) {
        Predaja predaja = predajaRepository.findById(request.getPredajaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Predaja: " + request.getPredajaId()));

        drziPredmetService.proveriPristup(predaja);

        Korisnik nastavnik = nastavnikRepository.findById(nastavnikId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Korisnik(Nastavnik): " + nastavnikId));

        if (request.getLinijaOd() > request.getLinijaDo()) {
            throw new BadRequestException("Početna linija ne može biti veća od krajnje linije.");
        }

        Komentar komentar = new Komentar();
        komentar.setPredaja(predaja);
        komentar.setNazivFajla(request.getNazivFajla());
        komentar.setLinijaOd(request.getLinijaOd());
        komentar.setLinijaDo(request.getLinijaDo());
        komentar.setTekst(request.getTekst());
        komentar.setNastavnik(nastavnik);

        return komentarRepository.save(komentar);
    }

    // student vidi samo komentare na svojim predajama; predajaId je opcioni filter
    public List<Komentar> findZaStudenta(Long studentId, Long predajaId) {
        if (predajaId == null) {
            return komentarRepository.findByPredajaStudentKorisnikId(studentId);
        }
        return komentarRepository.findByPredajaPredajaIdAndPredajaStudentKorisnikId(predajaId, studentId);
    }

    public List<Komentar> findByPredaja(Long predajaId) {
        Predaja predaja = predajaRepository.findById(predajaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Predaja: " + predajaId));
        drziPredmetService.proveriPristup(predaja);
        return komentarRepository.findByPredajaPredajaId(predajaId);
    }

    // komentari koje je ostavio ulogovani nastavnik
    public List<Komentar> findByNastavnik(Long nastavnikId) {
        return komentarRepository.findByNastavnikKorisnikId(nastavnikId);
    }

    public Komentar findById(Long id) {
        return komentarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Komentar sa id: " + id));
    }
}
