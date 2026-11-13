package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Komentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long komentarId;
    @ManyToOne
    private Predaja predaja;
    private String nazivFajla;
    private Integer linijaOd; // početna linija koda
    private Integer linijaDo; // krajnja linija koda (ako je ista, to je jedna linija)
    private String tekst;
    @ManyToOne
    private Korisnik nastavnik;   // koji nastavnik je ostavio komentar
}
