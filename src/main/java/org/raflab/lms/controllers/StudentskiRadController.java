package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.StudentskiRadRequest;
import org.raflab.lms.controllers.response.StudentskiRadResponse;
import org.raflab.lms.service.StudentskiRadService;
import org.raflab.lms.utils.Converters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studentskirad")
@AllArgsConstructor
public class StudentskiRadController {
    final StudentskiRadService studentskiRadService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long add(@Valid @ModelAttribute StudentskiRadRequest request) {
        return studentskiRadService.save(request).getStudentskiRadId();
    }

    @GetMapping("/all")
    public List<StudentskiRadResponse> getAll() {
        return Converters.toList(studentskiRadService.findAll(), Converters::toStudentskiRadResponse);
    }

    @GetMapping("/{id}")
    public StudentskiRadResponse getById(@PathVariable Long id) {
        return Converters.toStudentskiRadResponse(studentskiRadService.findById(id));
    }

    @GetMapping("/grupa/{grupaId}")
    public List<StudentskiRadResponse> getByGrupa(@PathVariable Long grupaId) {
        return Converters.toList(studentskiRadService.findByGrupa(grupaId), Converters::toStudentskiRadResponse);
    }
}