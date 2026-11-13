package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DrziPredmetRequest {
    @NotNull
    private Long nastavnikId;
    @NotNull
    private Long predmetId;
}