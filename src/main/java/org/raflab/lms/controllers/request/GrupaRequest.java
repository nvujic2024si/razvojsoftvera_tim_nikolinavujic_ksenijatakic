package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrupaRequest {
    @NotBlank
    private String nazivGrupe;
    @NotNull
    private Long terminId;
}
