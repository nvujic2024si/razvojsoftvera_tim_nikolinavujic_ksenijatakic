package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.ProveraRequest;
import org.raflab.lms.controllers.response.ProveraResponse;
import org.raflab.lms.service.ProveraService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provera")
@AllArgsConstructor
public class ProveraController {
    final ProveraService proveraService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid ProveraRequest request) {
        return proveraService.save(request).getProveraId();
    }

    @GetMapping("/all")
    public List<ProveraResponse> getAll() {
        return Converters.toList(proveraService.findAll(), Converters::toProveraResponse);
    }

    @GetMapping("/{id}")
    public ProveraResponse getById(@PathVariable Long id) {
        return Converters.toProveraResponse(proveraService.findById(id));
    }

    @GetMapping("/predmet/{predmetId}")
    public List<ProveraResponse> getByPredmet(@PathVariable Long predmetId) {
        return Converters.toList(proveraService.findByPredmet(predmetId), Converters::toProveraResponse);
    }

    @GetMapping("/rok/{rokId}")
    public List<ProveraResponse> getByRok(@PathVariable Long rokId) {
        return Converters.toList(proveraService.findByRok(rokId), Converters::toProveraResponse);
    }
}