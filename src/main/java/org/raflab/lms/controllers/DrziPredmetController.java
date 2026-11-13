package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.DrziPredmetRequest;
import org.raflab.lms.controllers.response.DrziPredmetResponse;
import org.raflab.lms.service.DrziPredmetService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drzipredmet")
@AllArgsConstructor
public class DrziPredmetController {
    final DrziPredmetService drziPredmetService;

    @PostMapping("/add")
    public Long addDrziPredmet(@RequestBody @Valid DrziPredmetRequest request) {
        return drziPredmetService.save(request).getDrziPredmetId();
    }

    @GetMapping("/all")
    public List<DrziPredmetResponse> getAll() {
        return Converters.toList(drziPredmetService.findAll(), Converters::toDrziPredmetResponse);
    }

    @GetMapping("/{id}")
    public DrziPredmetResponse getById(@PathVariable Long id) {
        return Converters.toDrziPredmetResponse(drziPredmetService.findById(id));
    }

    // predmeti koje drzi ulogovani nastavnik
    @GetMapping("/moji-predmeti")
    public List<DrziPredmetResponse> getMojiPredmeti() {
        return Converters.toList(drziPredmetService.mojiPredmeti(), Converters::toDrziPredmetResponse);
    }
}
