package org.raflab.lms.controllers.response;

import lombok.Data;
import org.raflab.lms.model.Uloga;

@Data
public class KorisnikResponse {
    private Long korisnikId;
    private String ime;
    private String prezime;
    private String brojIndeksa;
    private Uloga uloga;
    private String email;
    // lozinka NE — nikad ne vracamo hes u response
}
