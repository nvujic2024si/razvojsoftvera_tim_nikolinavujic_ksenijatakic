package org.raflab.lms.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.raflab.lms.model.Uloga;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    public static final String CLAIM_ULOGA = "uloga";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()

                        // sifarnici koje vide svi prijavljeni
                        .requestMatchers(HttpMethod.GET, "/api/predmet/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/rok/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/provera/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/termin/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/grupa/**").authenticated()
                        // specificnija pravila idu pre opstijih, redosled je bitan
                        .requestMatchers(HttpMethod.GET, "/api/drzipredmet/moji-predmeti").hasRole(Uloga.NASTAVNIK.name())
                        .requestMatchers(HttpMethod.GET, "/api/drzipredmet/**").authenticated()

                        // samo student: svoje podatke i akcije
                        .requestMatchers(HttpMethod.GET, "/api/prijava-za-proveru/moje-prijave").hasRole(Uloga.STUDENT.name())
                        .requestMatchers(HttpMethod.GET, "/api/predaja/moje-predaje").hasRole(Uloga.STUDENT.name())
                        .requestMatchers(HttpMethod.GET, "/api/komentar/moji-komentari").hasRole(Uloga.STUDENT.name())
                        .requestMatchers(HttpMethod.POST, "/api/predaja/preuzmi").hasRole(Uloga.STUDENT.name())
                        .requestMatchers(HttpMethod.GET, "/api/predaja/*/zadatak").hasRole(Uloga.STUDENT.name())
                        .requestMatchers(HttpMethod.POST, "/api/predaja/*/predaj").hasRole(Uloga.STUDENT.name())

                        // sve ostalo (upravljanje, spisak, pregled resenja, komentari, poeni, monitoring) je za nastavnika
                        .anyRequest().hasRole(Uloga.NASTAVNIK.name()))
                .oauth2ResourceServer(o -> o.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName(CLAIM_ULOGA);
        authorities.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    @Bean
    public SecretKey jwtKey(SecurityProperties props) {
        byte[] secret = props.jwtSecret().getBytes(StandardCharsets.UTF_8);
        if (secret.length < 32) {
            throw new IllegalStateException("app.security.jwt-secret mora imati najmanje 32 bajta");
        }
        return new SecretKeySpec(secret, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtKey) {
        return NimbusJwtDecoder.withSecretKey(jwtKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties props) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(props.corsOrigins());
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}