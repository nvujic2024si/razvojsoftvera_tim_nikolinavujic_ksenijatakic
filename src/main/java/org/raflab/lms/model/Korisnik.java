package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "lozinka")
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long korisnikId;
    private String ime;
    private String prezime;
    private String brojIndeksa; // ako je Uloga tip Nastavnik, ovo polje ostaje null
    @Enumerated(EnumType.STRING)
    private Uloga uloga;
    private String email;
    private String lozinka;
}
