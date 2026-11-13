package org.raflab.lms.controllers.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PredajaResponse {
    private Long predajaId;
    private KorisnikResponse student;
    private StudentskiRadResponse rad;
    private Integer poeni;
    private LocalDateTime vremePreuzimanja;
    private LocalDateTime vremePredaje;
}