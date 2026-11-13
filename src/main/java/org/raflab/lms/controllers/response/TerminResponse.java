package org.raflab.lms.controllers.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminResponse {
    private Long terminId;
    private String ucionica;
    private LocalDateTime vremePocetka;
    private LocalDateTime vremeZavrsetka;
    private ProveraResponse provera;
}
