package org.raflab.lms.controllers.response;

import lombok.Data;

@Data
public class KomentarResponse {
    private Long komentarId;
    private PredajaResponse predaja;
    private String nazivFajla;
    private Integer linijaOd;
    private Integer linijaDo;
    private String tekst;
    private KorisnikResponse nastavnik;
}
