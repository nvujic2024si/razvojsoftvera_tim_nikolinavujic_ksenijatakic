package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
// student ima najvise jednu predaju po zadatku
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_korisnik_id", "rad_studentski_rad_id"}))
public class Predaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long predajaId;
    @ManyToOne
    private Korisnik student;
    @ManyToOne
    private StudentskiRad rad;
    private String putanjaDoPredaje; // postavlja server, klijent je nikad ne salje
    private Integer poeni; // null dok nastavnik ne oceni
    private LocalDateTime vremePreuzimanja;
    private LocalDateTime vremePredaje;
}