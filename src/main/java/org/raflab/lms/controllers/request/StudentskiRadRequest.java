package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StudentskiRadRequest {
    @NotBlank
    private String nazivZadatka;
    @NotNull
    private MultipartFile fajl; // zip projekta
    @NotNull
    private Long grupaId;
}