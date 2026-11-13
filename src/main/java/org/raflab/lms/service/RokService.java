package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.RokRequest;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Rok;
import org.raflab.lms.repository.RokRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class RokService {
    private final RokRepository rokRepository;

    @Transactional
    public Rok save(RokRequest request) {
        Rok rok = Converters.toRok(request);
        return rokRepository.save(rok);
    }

    public List<Rok> findAll() {
        return rokRepository.findAll();
    }

    public Rok findById(Long id) {
        return rokRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Rok sa id: " + id));
    }
}
