package org.raflab.lms.controllers.response;

import lombok.Data;

@Data
public class GrupaResponse {
    private Long grupaId;
    private String nazivGrupe;
    private TerminResponse termin;
}
