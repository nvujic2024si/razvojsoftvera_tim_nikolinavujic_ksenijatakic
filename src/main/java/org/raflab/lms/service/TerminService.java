package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.TerminRequest;
import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Provera;
import org.raflab.lms.model.Termin;
import org.raflab.lms.repository.ProveraRepository;
import org.raflab.lms.repository.TerminRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class TerminService {
    private final TerminRepository terminRepository;
    private final ProveraRepository proveraRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public Termin save(TerminRequest request) {
        if (!request.getVremeZavrsetka().isAfter(request.getVremePocetka())) {
            throw new BadRequestException("Vreme zavrsetka mora biti posle vremena pocetka.");
        }

        Provera provera = proveraRepository.findById(request.getProveraId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Provera: " + request.getProveraId()));

        drziPredmetService.proveriPristup(provera);

        Termin termin = Converters.toTermin(request);
        termin.setProvera(provera);
        return terminRepository.save(termin);
    }

    public List<Termin> findAll() {
        return terminRepository.findAll();
    }

    public Termin findById(Long id) {
        return terminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Termin sa id: " + id));
    }

    public List<Termin> findByProvera(Long proveraId) {
        return terminRepository.findByProveraProveraId(proveraId);
    }
}
