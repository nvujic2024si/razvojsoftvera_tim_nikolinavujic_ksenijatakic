package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.lms.model.TipProvere;

@Data
public class ProveraRequest {
    @NotNull
    private TipProvere tipProvere;
    @NotNull
    private Long predmetId;
    @NotNull
    private Long rokId;
}
