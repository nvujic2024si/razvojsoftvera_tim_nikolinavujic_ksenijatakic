package org.raflab.lms.controllers.response;

import lombok.Data;
import org.raflab.lms.model.StatusStudenta;

import java.time.LocalDateTime;

@Data
public class StudentStatusResponse {
    private KorisnikResponse student;
    private String grupa;
    private StatusStudenta status;
    private LocalDateTime vremePreuzimanja;
    private LocalDateTime vremePredaje;
    private Long trajanjeMinuta; // samo kad je predat
}