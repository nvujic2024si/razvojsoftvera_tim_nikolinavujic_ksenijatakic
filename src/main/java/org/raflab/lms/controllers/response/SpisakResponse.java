package org.raflab.lms.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SpisakResponse {
    private int dodato;
    private int vecPostojalo; // vec su u ovoj grupi
    private List<String> uDrugojGrupi; // vec su u drugoj grupi iste provere, ne diramo ih
    private List<String> nepoznatiIndeksi;
}