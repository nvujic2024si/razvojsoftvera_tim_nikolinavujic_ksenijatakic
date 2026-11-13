package org.raflab.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Rok {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rokId;
    private String nazivRoka; // ako je kolokvijum -> kolokvijum 1 ili 2, ako je ispit -> mesec
    private int godina;
}
