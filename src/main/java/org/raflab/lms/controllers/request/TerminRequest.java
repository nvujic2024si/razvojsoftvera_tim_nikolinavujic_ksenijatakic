package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminRequest {
    @NotBlank
    private String ucionica;
    @NotNull
    private LocalDateTime vremePocetka;
    @NotNull
    private LocalDateTime vremeZavrsetka;
    @NotNull
    private Long proveraId;
}
