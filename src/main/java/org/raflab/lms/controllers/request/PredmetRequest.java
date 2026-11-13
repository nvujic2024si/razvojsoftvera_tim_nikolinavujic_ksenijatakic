package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PredmetRequest {
    @NotBlank
    private String nazivPredmeta;
    @NotBlank
    private String kodPredmeta;
}