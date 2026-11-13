package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.GrupaRequest;
import org.raflab.lms.controllers.response.GrupaResponse;
import org.raflab.lms.service.GrupaService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupa")
@AllArgsConstructor
public class GrupaController {
    final GrupaService grupaService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid GrupaRequest request) {
        return grupaService.save(request).getGrupaId();
    }

    @GetMapping("/all")
    public List<GrupaResponse> getAll() {
        return Converters.toList(grupaService.findAll(), Converters::toGrupaResponse);
    }

    @GetMapping("/{id}")
    public GrupaResponse getById(@PathVariable Long id) {
        return Converters.toGrupaResponse(grupaService.findById(id));
    }

    @GetMapping("/termin/{terminId}")
    public List<GrupaResponse> getByTermin(@PathVariable Long terminId) {
        return Converters.toList(grupaService.findByTermin(terminId), Converters::toGrupaResponse);
    }
}