package org.raflab.lms.controllers.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.raflab.lms.model.Uloga;

@Data
public class KorisnikRequest {
    @NotBlank
    private String ime;
    @NotBlank
    private String prezime;
    private String brojIndeksa;
    @NotBlank
    @Email
    private String email;
    @Size(min = 8)
    private String lozinka;
    @NotNull
    private Uloga uloga;
}
