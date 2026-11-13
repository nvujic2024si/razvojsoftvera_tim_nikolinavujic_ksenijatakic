package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KomentarRequest {
    @NotNull
    private Long predajaId;
    @NotBlank
    private String nazivFajla;
    @NotNull
    @Min(1)
    private Integer linijaOd;
    @NotNull
    @Min(1)
    private Integer linijaDo;
    @NotBlank
    private String tekst;
    // nastavnik se izvlaci iz JWT tokena, ne iz requesta
}
