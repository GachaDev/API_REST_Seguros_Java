package com.es.segurosinseguros.security;

import com.es.segurosinseguros.exception.DataBaseException;
import com.es.segurosinseguros.model.AsistenciaMedica;
import com.es.segurosinseguros.model.Seguro;
import com.es.segurosinseguros.repository.AsistenciaMedicaRepository;
import com.es.segurosinseguros.repository.SeguroRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private RsaKeyProperties rsaKeys;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private AsistenciaMedicaRepository asistenciaMedicaRepository;

    private AuthorizationManager<RequestAuthorizationContext> getSeguroByIdManager() {
        return (authentication, object) -> {
            Authentication auth = authentication.get();

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return new AuthorizationDecision(true);
            }

            String path = object.getRequest().getRequestURI();
            String id = path.replaceAll("/seguros/", "");
            Long idS = 0L;

            try {
                idS = Long.parseLong(id);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id debe de ser un número correcto");
            }

            Seguro seguro = null;

            try {
                seguro = seguroRepository.findById(idS).orElse(null);
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            if (seguro == null) {
                return new AuthorizationDecision(false);
            }

            if (seguro.getUsuario().getUsername().equals(auth.getName())) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> getAsistenciaByIdManager() {
        return (authentication, object) -> {
            Authentication auth = authentication.get();

            System.out.println("Usuario autenticado: " + auth.getName());

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return new AuthorizationDecision(true);
            }

            String path = object.getRequest().getRequestURI();
            String id = path.replaceAll("/asistencias/", "");
            Long idS = 0L;

            try {
                idS = Long.parseLong(id);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id debe de ser un número correcto");
            }

            AsistenciaMedica asistenciaMedica = null;

            try {
                asistenciaMedica = asistenciaMedicaRepository.findById(idS).orElse(null);
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            if (asistenciaMedica == null) {
                return new AuthorizationDecision(false);
            }

            if (asistenciaMedica.getSeguro().getUsuario().getUsername().equals(auth.getName())) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/login", "/usuarios/register").permitAll()
                        .requestMatchers("/seguros/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/seguros/{id}").access(getSeguroByIdManager())
                        .requestMatchers(HttpMethod.PUT, "/seguros/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/seguros/{id}").hasRole("ADMIN")
                        .requestMatchers("/asistencias/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/asistencias/{id}").access(getAsistenciaByIdManager())                        .requestMatchers("/asistencias/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/asistencias/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/asistencias/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter() {
                            {
                                setJwtGrantedAuthoritiesConverter(new CustomJwtAuthenticationConverter());
                            }
                        }))
                )                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }
}