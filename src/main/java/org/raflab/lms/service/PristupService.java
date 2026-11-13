package org.raflab.lms.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class PristupService {
    public Long ulogovaniKorisnikId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            Object id = jwt.getClaim("korisnikId");
            return id instanceof Number n ? n.longValue() : null;
        }
        return null;
    }
}