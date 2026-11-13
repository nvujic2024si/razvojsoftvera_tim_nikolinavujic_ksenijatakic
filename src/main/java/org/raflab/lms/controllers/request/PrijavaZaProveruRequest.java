package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrijavaZaProveruRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long proveraId;
    @NotNull
    private Long grupaId;
}
