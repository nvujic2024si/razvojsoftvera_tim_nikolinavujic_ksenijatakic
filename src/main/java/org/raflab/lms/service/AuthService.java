package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.config.SecurityConfig;
import org.raflab.lms.config.SecurityProperties;
import org.raflab.lms.exceptions.UnauthorizedException;
import org.raflab.lms.model.Korisnik;
import org.raflab.lms.model.Uloga;
import org.raflab.lms.repository.KorisnikRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@AllArgsConstructor
@Service
public class AuthService {

    public static final String CLAIM_KORISNIK_ID = "korisnikId";

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;

    public record Prijava(String token, Instant istice, Uloga uloga) {
    }

    @Transactional(readOnly = true)
    public Prijava login(String email, String lozinka) {
        Korisnik korisnik = korisnikRepository.findByEmailIgnoreCase(email)
                .filter(k -> k.getLozinka() != null && passwordEncoder.matches(lozinka, k.getLozinka()))
                .orElseThrow(() -> new UnauthorizedException("Pogresan email ili lozinka"));
        return izdajToken(korisnik);
    }

    private Prijava izdajToken(Korisnik korisnik) {
        Instant sada = Instant.now();
        Instant istice = sada.plus(securityProperties.jwtTrajanje());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("lms")
                .subject(korisnik.getEmail())
                .issuedAt(sada)
                .expiresAt(istice)
                .claim(SecurityConfig.CLAIM_ULOGA, korisnik.getUloga().name())
                .claim(CLAIM_KORISNIK_ID, korisnik.getKorisnikId())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new Prijava(token, istice, korisnik.getUloga());
    }
}