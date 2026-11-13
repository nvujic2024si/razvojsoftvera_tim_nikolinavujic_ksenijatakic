package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.LoginRequest;
import org.raflab.lms.controllers.response.LoginResponse;
import org.raflab.lms.service.AuthService;
import org.raflab.lms.utils.Converters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/auth")
public class AuthController {

    final AuthService authService;

    @PostMapping(path = "/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return Converters.toLoginResponse(authService.login(request.getEmail(), request.getLozinka()));
    }
}