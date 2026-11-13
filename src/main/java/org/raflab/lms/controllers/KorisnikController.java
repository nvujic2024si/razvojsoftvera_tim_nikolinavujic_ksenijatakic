package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.KorisnikRequest;
import org.raflab.lms.controllers.response.KorisnikResponse;
import org.raflab.lms.service.KorisnikService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/korisnik")
@AllArgsConstructor
public class KorisnikController {
    final KorisnikService korisnikService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid KorisnikRequest request) {
        return korisnikService.save(request).getKorisnikId();
    }

    @GetMapping("/all")
    public List<KorisnikResponse> getAll() {
        return Converters.toList(korisnikService.findAll(), Converters::toKorisnikResponse);
    }

    @GetMapping("/{id}")
    public KorisnikResponse getById(@PathVariable Long id) {
        return Converters.toKorisnikResponse(korisnikService.findById(id));
    }
}