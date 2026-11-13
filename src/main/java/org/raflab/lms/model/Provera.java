package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Provera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proveraId;
    @Enumerated(EnumType.STRING)
    private TipProvere tipProvere;
    @ManyToOne
    private Predmet predmet;
    @ManyToOne
    private Rok rok;
}
