package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.StudentskiRadRequest;
import org.raflab.lms.exceptions.ConflictException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Grupa;
import org.raflab.lms.model.StudentskiRad;
import org.raflab.lms.repository.GrupaRepository;
import org.raflab.lms.repository.StudentskiRadRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentskiRadService {
    private final StudentskiRadRepository studentskiRadRepository;
    private final GrupaRepository grupaRepository;
    private final FileStorageService fileStorageService;
    private final DrziPredmetService drziPredmetService;

    @Transactional
    public StudentskiRad save(StudentskiRadRequest request) {
        Grupa grupa = grupaRepository.findById(request.getGrupaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Grupa: " + request.getGrupaId()));

        drziPredmetService.proveriPristup(grupa);

        if (studentskiRadRepository.existsByGrupaGrupaId(grupa.getGrupaId())) {
            throw new ConflictException("Grupa vec ima postavljen zadatak");
        }

        StudentskiRad rad = Converters.toStudentskiRad(request);
        rad.setGrupa(grupa);
        rad = studentskiRadRepository.save(rad); // treba nam id za putanju fajla

        // ako cuvanje fajla pukne, transakcija se vraca i red u bazi nestaje
        rad.setPutanjaDoFajla(fileStorageService.sacuvajZadatak(rad.getStudentskiRadId(), request.getFajl()));
        return studentskiRadRepository.save(rad);
    }

    public List<StudentskiRad> findAll() {
        return studentskiRadRepository.findAll();
    }

    public StudentskiRad findById(Long id) {
        return studentskiRadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji StudentskiRad sa id: " + id));
    }

    public List<StudentskiRad> findByGrupa(Long grupaId) {
        return studentskiRadRepository.findByGrupaGrupaId(grupaId);
    }
}