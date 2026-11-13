package org.raflab.lms.controllers.response;

import lombok.Data;

@Data
public class DrziPredmetResponse {
    private Long drziPredmetId;
    private KorisnikResponse nastavnik;
    private PredmetResponse predmet;
}