package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.RokRequest;
import org.raflab.lms.controllers.response.RokResponse;
import org.raflab.lms.service.RokService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rok")
@AllArgsConstructor
public class RokController {
    final RokService rokService;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid RokRequest request) {
        return rokService.save(request).getRokId();
    }

    @GetMapping("/all")
    public List<RokResponse> getAll() {
        return Converters.toList(rokService.findAll(), Converters::toRokResponse);
    }

    @GetMapping("/{id}")
    public RokResponse getById(@PathVariable Long id) {
        return Converters.toRokResponse(rokService.findById(id));
    }
}