package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PrijavaZaProveruRequest;
import org.raflab.lms.controllers.request.SpisakRequest;
import org.raflab.lms.controllers.response.PrijavaZaProveruResponse;
import org.raflab.lms.controllers.response.SpisakResponse;
import org.raflab.lms.service.PrijavaZaProveruService;
import org.raflab.lms.service.PristupService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prijava-za-proveru")
@AllArgsConstructor
public class PrijavaZaProveruController {
    final PrijavaZaProveruService prijavaService;
    final PristupService pristupService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid PrijavaZaProveruRequest request) {
        return prijavaService.save(request).getPrijavaId();
    }

    @GetMapping("/all")
    public List<PrijavaZaProveruResponse> getAll() {
        return Converters.toList(prijavaService.findAll(), Converters::toPrijavaZaProveruResponse);
    }

    @GetMapping("/{id}")
    public PrijavaZaProveruResponse getById(@PathVariable Long id) {
        return Converters.toPrijavaZaProveruResponse(prijavaService.findById(id));
    }

    @GetMapping("/moje-prijave")
    public List<PrijavaZaProveruResponse> getMojePrijave() {
        Long korisnikId = pristupService.ulogovaniKorisnikId();
        return Converters.toList(prijavaService.findByStudent(korisnikId), Converters::toPrijavaZaProveruResponse);
    }

    @GetMapping("/grupa/{grupaId}")
    public List<PrijavaZaProveruResponse> getByGrupa(@PathVariable Long grupaId) {
        return Converters.toList(prijavaService.findByGrupa(grupaId), Converters::toPrijavaZaProveruResponse);
    }

    // nastavnik unosi spisak studenata za grupu
    @PostMapping("/spisak")
    public SpisakResponse unesiSpisak(@RequestBody @Valid SpisakRequest request) {
        return prijavaService.unesiSpisak(request);
    }
}