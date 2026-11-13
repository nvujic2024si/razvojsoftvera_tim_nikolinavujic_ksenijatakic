package org.raflab.lms.controllers.response;

import lombok.Data;

@Data
public class StudentskiRadResponse {
    private Long studentskiRadId;
    private String nazivZadatka;
    private GrupaResponse grupa;
}