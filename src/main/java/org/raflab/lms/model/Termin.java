package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long terminId;
    private String ucionica;
    private LocalDateTime vremePocetka;
    private LocalDateTime vremeZavrsetka;
    @ManyToOne
    private Provera provera;
}
