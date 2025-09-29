package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.LoginRequest;
import com.vini334.dashboard.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserDetailsService uds;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, UserDetailsService uds, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.uds = uds;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        try {
            var token = new UsernamePasswordAuthenticationToken(body.email(), body.password());
            authManager.authenticate(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciais invalidas"));
        }

        UserDetails user = uds.loadUserByUsername(body.email());
        String roles = Optional.ofNullable(user.getAuthorities())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        String jwt = jwtUtil.generateToken(user.getUsername(), roles);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}