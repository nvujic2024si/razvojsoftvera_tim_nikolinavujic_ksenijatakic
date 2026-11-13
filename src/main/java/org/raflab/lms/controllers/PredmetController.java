package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PredmetRequest;
import org.raflab.lms.controllers.response.PredmetResponse;
import org.raflab.lms.service.PredmetService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmet")
@AllArgsConstructor
public class PredmetController {
    final PredmetService predmetService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid PredmetRequest request) {
        return predmetService.save(request).getPredmetId();
    }

    @GetMapping("/all")
    public List<PredmetResponse> getAll() {
        return Converters.toList(predmetService.findAll(), Converters::toPredmetResponse);
    }

    @GetMapping("/{id}")
    public PredmetResponse getById(@PathVariable Long id) {
        return Converters.toPredmetResponse(predmetService.findById(id));
    }
}