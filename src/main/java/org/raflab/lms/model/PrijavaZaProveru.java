package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
// student polaze proveru u najvise jednoj grupi
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_korisnik_id", "provera_provera_id"}))
public class PrijavaZaProveru {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prijavaId;
    @ManyToOne
    private Korisnik student;
    @ManyToOne
    private Provera provera;
    @ManyToOne
    private Grupa grupa;
}