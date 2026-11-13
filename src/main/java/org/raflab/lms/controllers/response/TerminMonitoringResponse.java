package org.raflab.lms.controllers.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TerminMonitoringResponse {
    private Long terminId;
    private String ucionica;
    private LocalDateTime vremePocetka;
    private LocalDateTime vremeZavrsetka;
    private int ukupnoPrijavljenih;
    private int ukupnoPreuzeto;
    private int ukupnoPredato;
    private Double prosecnoVremeIzradeMinuta; // null dok niko nije predao
    private List<StudentStatusResponse> studenti;
}