package com.pgc.stress_predict;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgc.stress_predict.application.dto.request.AuthLoginRequest;
import com.pgc.stress_predict.application.dto.request.UsuarioFormRequest;
import com.pgc.stress_predict.application.dto.response.AuthResponse;
import com.pgc.stress_predict.application.service.AuthService;
import com.pgc.stress_predict.infrastructure.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. log-in exitoso
    @Test
    void login_shouldReturn200() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("test@example.com", "password");
        AuthResponse response = new AuthResponse("test@example.com", "ok", "jwt-token", true);

        when(authService.loginUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/auth/log-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test@example.com"));
    }

    // 2. log-in con email inválido
    @Test
    void login_withInvalidEmail_shouldReturn400() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("invalid-email", "1234");

        mockMvc.perform(post("/api/auth/log-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // 3. log-in con campos vacíos
    @Test
    void login_withBlankFields_shouldReturn400() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("", "");

        mockMvc.perform(post("/api/auth/log-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // 4. sign-up exitoso
    @Test
    void register_shouldReturn201_andAuthResponse() throws Exception {
        UsuarioFormRequest request = new UsuarioFormRequest(
                "Juan", "Pérez", 12345678, LocalDate.of(2000, 1, 1),
                "123456789", "juan@example.com", "1234", 2f, 1f, 7f, 1.5f, 1f, 9f
        );

        AuthResponse response = new AuthResponse("Juan", "Usuario registrado exitosamente", "jwt-token", true);

        when(authService.registerUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/auth/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Juan"))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"))
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.success").value(true));
    }

    // 5. sign-up con email vacío
    @Test
    void register_withMissingEmail_shouldReturn400() throws Exception {
        UsuarioFormRequest request = new UsuarioFormRequest(
                "Ana", "López", 12345678, LocalDate.of(2000, 1, 1),
                "987654321", "", "1234", 2f, 1f, 7f, 1.5f, 1f, 9f
        );

        mockMvc.perform(post("/api/auth/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // 6. log-in - excepción controlada en servicio
    @Test
    void login_shouldReturn401_whenBadCredentials() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("user@example.com", "wrongpass");

        when(authService.loginUser(request)).thenThrow(new BadCredentialsException("Contraseña invalida"));

        mockMvc.perform(post("/api/auth/log-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}

