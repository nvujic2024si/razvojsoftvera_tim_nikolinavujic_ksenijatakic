package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.ProveraRequest;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Rok;
import org.raflab.lms.model.Predmet;
import org.raflab.lms.model.Provera;
import org.raflab.lms.repository.RokRepository;
import org.raflab.lms.repository.PredmetRepository;
import org.raflab.lms.repository.ProveraRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ProveraService {
    private final ProveraRepository proveraRepository;
    private final PredmetRepository predmetRepository;
    private final RokRepository rokRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public Provera save(ProveraRequest request) {
        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji predmet: " + request.getPredmetId()));

        drziPredmetService.proveriPristup(predmet.getPredmetId());

        Rok rok = rokRepository.findById(request.getRokId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Rok: " + request.getRokId()));

        Provera provera = Converters.toProvera(request);
        provera.setPredmet(predmet);
        provera.setRok(rok);
        return proveraRepository.save(provera);
    }

    public List<Provera> findAll() {
        return proveraRepository.findAll();
    }

    public Provera findById(Long id) {
        return proveraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Provera sa id: " + id));
    }

    public List<Provera> findByPredmet(Long predmetId) {
        return proveraRepository.findByPredmetPredmetId(predmetId);
    }

    public List<Provera> findByRok(Long rokId) {
        return proveraRepository.findByRokRokId(rokId);
    }
}