package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.KomentarRequest;
import org.raflab.lms.controllers.response.KomentarResponse;
import org.raflab.lms.service.KomentarService;
import org.raflab.lms.service.PristupService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/komentar")
@AllArgsConstructor
public class KomentarController {
    final KomentarService komentarService;
    final PristupService pristupService;

    // nastavnik

    @PostMapping("/add")
    public Long add(@RequestBody @Valid KomentarRequest request) {
        Long nastavnikId = pristupService.ulogovaniKorisnikId();
        return komentarService.save(request, nastavnikId).getKomentarId();
    }

    @GetMapping("/predaja/{predajaId}")
    public List<KomentarResponse> getByPredaja(@PathVariable Long predajaId) {
        return Converters.toList(komentarService.findByPredaja(predajaId), Converters::toKomentarResponse);
    }

    // komentari koje je ulogovani nastavnik ostavio
    @GetMapping("/ostavljeni")
    public List<KomentarResponse> getOstavljeni() {
        Long nastavnikId = pristupService.ulogovaniKorisnikId();
        return Converters.toList(komentarService.findByNastavnik(nastavnikId), Converters::toKomentarResponse);
    }

    @GetMapping("/{id}")
    public KomentarResponse getById(@PathVariable Long id) {
        return Converters.toKomentarResponse(komentarService.findById(id));
    }

    // student

    // komentari na moje predaje
    @GetMapping("/moji-komentari")
    public List<KomentarResponse> getMojiKomentari(@RequestParam(required = false) Long predajaId) {
        Long studentId = pristupService.ulogovaniKorisnikId();
        return Converters.toList(komentarService.findZaStudenta(studentId, predajaId), Converters::toKomentarResponse);
    }
}