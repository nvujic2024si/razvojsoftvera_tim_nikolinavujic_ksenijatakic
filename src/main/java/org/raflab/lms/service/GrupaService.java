package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.GrupaRequest;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Grupa;
import org.raflab.lms.model.Termin;
import org.raflab.lms.repository.GrupaRepository;
import org.raflab.lms.repository.TerminRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class GrupaService {
    private final GrupaRepository grupaRepository;
    private final TerminRepository terminRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public Grupa save(GrupaRequest request) {
        Termin termin = terminRepository.findById(request.getTerminId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Termin: " + request.getTerminId()));

        drziPredmetService.proveriPristup(termin);

        Grupa grupa = Converters.toGrupa(request);
        grupa.setTermin(termin);
        return grupaRepository.save(grupa);
    }

    public List<Grupa> findAll() {
        return grupaRepository.findAll();
    }

    public Grupa findById(Long id) {
        return grupaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Grupa sa id: " + id));
    }

    public List<Grupa> findByTermin(Long terminId) {
        return grupaRepository.findByTerminTerminId(terminId);
    }
}
