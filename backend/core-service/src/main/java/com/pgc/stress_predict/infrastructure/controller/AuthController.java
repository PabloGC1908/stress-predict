package com.pgc.stress_predict.infrastructure.controller;

import com.pgc.stress_predict.application.dto.request.AuthLoginRequest;
import com.pgc.stress_predict.application.dto.response.AuthResponse;
import com.pgc.stress_predict.application.dto.request.UsuarioFormRequest;
import com.pgc.stress_predict.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userDetailsService;

    public AuthController(AuthService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UsuarioFormRequest registerUserRequest) {
        return new ResponseEntity<>(this.userDetailsService.registerUser(registerUserRequest), HttpStatus.CREATED);
    }
}