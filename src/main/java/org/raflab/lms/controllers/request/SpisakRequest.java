package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SpisakRequest {
    @NotNull
    private Long grupaId;
    @NotEmpty
    private List<@NotBlank String> brojeviIndeksa; // tacno kako su upisani kod studenta, npr. "RN 23/2024"
}