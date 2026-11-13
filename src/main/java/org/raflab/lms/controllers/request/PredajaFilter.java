package org.raflab.lms.controllers.request;

import lombok.Data;

@Data
public class PredajaFilter {
    private Long predmetId;
    private Long rokId;
    private Long terminId;
    private Long grupaId;
    private String ucionica;
    private Long studentId;
    private String brojIndeksa;
}