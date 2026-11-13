package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
// jedan zadatak po grupi
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "grupa_grupa_id"))
public class StudentskiRad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentskiRadId;
    private String nazivZadatka;
    private String putanjaDoFajla; // relativna putanja u storage folderu, postavlja server
    @ManyToOne
    private Grupa grupa;
}