package org.raflab.lms.controllers.response;

import lombok.Data;
import org.raflab.lms.model.TipProvere;

@Data
public class ProveraResponse {
    private Long proveraId;
    private TipProvere tipProvere;
    private PredmetResponse predmet;
    private RokResponse rok;
}
