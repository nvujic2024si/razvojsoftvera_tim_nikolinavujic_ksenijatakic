package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PredmetRequest;
import org.raflab.lms.exceptions.ConflictException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Predmet;
import org.raflab.lms.repository.PredmetRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PredmetService {
    private final PredmetRepository predmetRepository;

    @Transactional
    public Predmet save(PredmetRequest request) {
        if (predmetRepository.findByKodPredmeta(request.getKodPredmeta()).isPresent()) {
            throw new ConflictException("Predmet sa ovim kodom vec postoji: " + request.getKodPredmeta());
        }

        Predmet predmet = Converters.toPredmet(request);
        return predmetRepository.save(predmet);
    }

    public List<Predmet> findAll() {
        return predmetRepository.findAll();
    }

    public Predmet findById(Long id) {
        return predmetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Predmet sa id: " + id));
    }
}