package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreuzmiRequest {
    @NotNull
    private Long terminId;
    @NotNull
    private Long grupaId;
}