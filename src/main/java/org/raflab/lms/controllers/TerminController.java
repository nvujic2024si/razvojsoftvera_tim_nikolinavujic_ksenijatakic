package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.TerminRequest;
import org.raflab.lms.controllers.response.TerminResponse;
import org.raflab.lms.service.TerminService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/termin")
@AllArgsConstructor
public class TerminController {
    final TerminService terminService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid TerminRequest request) {
        return terminService.save(request).getTerminId();
    }

    @GetMapping("/all")
    public List<TerminResponse> getAll() {
        return Converters.toList(terminService.findAll(), Converters::toTerminResponse);
    }

    @GetMapping("/{id}")
    public TerminResponse getById(@PathVariable Long id) {
        return Converters.toTerminResponse(terminService.findById(id));
    }

    @GetMapping("/provera/{proveraId}")
    public List<TerminResponse> getByProvera(@PathVariable Long proveraId) {
        return Converters.toList(terminService.findByProvera(proveraId), Converters::toTerminResponse);
    }
}