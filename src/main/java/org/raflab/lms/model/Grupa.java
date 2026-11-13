package org.raflab.lms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Grupa {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long grupaId;
    private String nazivGrupe;
    @ManyToOne
    private Termin termin;

}
