package org.raflab.lms.controllers.response;

import lombok.Data;

@Data
public class PrijavaZaProveruResponse {
    private Long prijavaId;
    private KorisnikResponse student;
    private ProveraResponse provera;
    private GrupaResponse grupa;
}
