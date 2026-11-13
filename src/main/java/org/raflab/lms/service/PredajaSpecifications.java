package org.raflab.lms.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.raflab.lms.controllers.request.PredajaFilter;
import org.raflab.lms.model.*;
import org.raflab.lms.utils.IndeksUtil;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

// dinamicki filter za pretragu resenja: svaki kriterijum je opcion
// nastavnik uvek vidi samo svoje predmete

public class PredajaSpecifications {

    public static Specification<Predaja> filter(PredajaFilter f, List<Long> dozvoljeniPredmeti) {
        return (root, query, cb) -> {
            Join<Predaja, Korisnik> student = root.join("student");
            Join<Predaja, StudentskiRad> rad = root.join("rad");
            Join<StudentskiRad, Grupa> grupa = rad.join("grupa");
            Join<Grupa, Termin> termin = grupa.join("termin");
            Join<Termin, Provera> provera = termin.join("provera");

            List<Predicate> uslovi = new ArrayList<>();
            if (f.getStudentId() != null) {
                uslovi.add(cb.equal(student.get("korisnikId"), f.getStudentId()));
            }
            if (hasText(f.getBrojIndeksa())) {
                uslovi.add(cb.equal(student.get("brojIndeksa"), IndeksUtil.normalizuj(f.getBrojIndeksa())));
            }
            if (f.getGrupaId() != null) {
                uslovi.add(cb.equal(grupa.get("grupaId"), f.getGrupaId()));
            }
            if (f.getTerminId() != null) {
                uslovi.add(cb.equal(termin.get("terminId"), f.getTerminId()));
            }
            if (hasText(f.getUcionica())) {
                uslovi.add(cb.equal(cb.lower(termin.<String>get("ucionica")), f.getUcionica().trim().toLowerCase()));
            }
            if (f.getPredmetId() != null) {
                uslovi.add(cb.equal(provera.get("predmet").get("predmetId"), f.getPredmetId()));
            }
            if (f.getRokId() != null) {
                uslovi.add(cb.equal(provera.get("rok").get("rokId"), f.getRokId()));
            }

            // nastavnik vidi samo predmete koje drzi
            uslovi.add(provera.get("predmet").get("predmetId").in(dozvoljeniPredmeti));

            return cb.and(uslovi.toArray(new Predicate[0]));
        };
    }
}