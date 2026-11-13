package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RokRequest {
    @NotBlank
    private String nazivRoka;
    @NotNull
    private Integer godina;
}
