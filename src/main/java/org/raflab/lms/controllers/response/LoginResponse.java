package org.raflab.lms.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.lms.model.Uloga;

import java.time.Instant;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Instant istice;
    private Uloga uloga;
}
