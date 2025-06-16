package com.pgc.stress_predict;

import com.pgc.stress_predict.application.dto.request.AuthLoginRequest;
import com.pgc.stress_predict.application.dto.request.UsuarioFormRequest;
import com.pgc.stress_predict.application.dto.response.AuthResponse;
import com.pgc.stress_predict.application.mapper.UsuarioRegisterMapper;
import com.pgc.stress_predict.application.service.AuthService;
import com.pgc.stress_predict.domain.model.Rol;
import com.pgc.stress_predict.domain.model.RolEnum;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.model.UsuarioInfo;
import com.pgc.stress_predict.domain.port.out.UsuarioRepository;
import com.pgc.stress_predict.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRegisterMapper usuarioRegisterMapper;

    @InjectMocks
    private AuthService authService;

    // 1. loginUser - caso exitoso
    @Test
    void loginUser_successful() {
        AuthLoginRequest request = new AuthLoginRequest("test@example.com", "password");
        ZoneOffset offset = ZoneOffset.of("-05:00");

        Rol rol = Rol.builder()
                .id(1)
                .rol(RolEnum.USUARIO)
                .permisos(Set.of())
                .build();

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre("Test")
                .apellido("Example")
                .telefono("123456789")
                .dni(12345678)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .horasEstudioDia(2f)
                .horasExtracurricularDia(1f)
                .horasSuenoDia(7f)
                .horasSocialDia(1.5f)
                .horasActividadFisicaDia(1f)
                .promedioCalificaciones(9f)
                .build();

        Usuario userDetails = Usuario.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(usuarioRepository.findUsuarioByEmail("test@example.com")).thenReturn(1L);
        when(jwtService.generateToken(any(), eq(1L))).thenReturn("fake-jwt");

        AuthResponse response = authService.loginUser(request);

        assertEquals("test@example.com", response.username());
        assertEquals("Usuario logueado correctamente", response.message());
    }

    // 2. loginUser - usuario no existe
    @Test
    void loginUser_userNotFound_throwsException() {
        AuthLoginRequest request = new AuthLoginRequest("nonexistent@example.com", "password");

        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loginUser(request);
        });
    }

    // 3. loginUser - contraseña incorrecta
    @Test
    void loginUser_invalidPassword_throwsException() {
        AuthLoginRequest request = new AuthLoginRequest("test@example.com", "wrongpass");
        ZoneOffset offset = ZoneOffset.of("-05:00");

        Rol rol = Rol.builder()
                .id(1)
                .rol(RolEnum.USUARIO)
                .permisos(Set.of())
                .build();

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre("Test")
                .apellido("Example")
                .telefono("123456789")
                .dni(12345678)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .horasEstudioDia(2f)
                .horasExtracurricularDia(1f)
                .horasSuenoDia(7f)
                .horasSocialDia(1.5f)
                .horasActividadFisicaDia(1f)
                .promedioCalificaciones(9f)
                .build();

        Usuario userDetails = Usuario.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches("wrongpass", "hashedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authService.loginUser(request);
        });
    }

    // 4. registerUser - caso exitoso
    @Test
    void registerUser_successful() {
        UsuarioFormRequest request = new UsuarioFormRequest(
                "Juan", "Pérez", 12345678, LocalDate.of(2000, 1, 1), "123456789",
                "juan@example.com", "1234", 2.0f, 1.0f, 7.0f, 1.5f, 1.0f, 9.0f
        );

        Usuario mockUsuario = mock(Usuario.class);
        UsuarioInfo mockInfo = mock(UsuarioInfo.class);

        when(usuarioRegisterMapper.apply(request)).thenReturn(mockUsuario);
        when(usuarioRepository.save(mockUsuario)).thenReturn(mockUsuario);
        when(mockUsuario.getUsuarioInfo()).thenReturn(mockInfo);
        when(mockInfo.getNombre()).thenReturn("Juan");
        when(mockUsuario.getId()).thenReturn(10L);
//        when(mockUsuario.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));
        when(jwtService.generateToken(any(), eq(10L))).thenReturn("jwt-token");

        AuthResponse response = authService.registerUser(request);

        assertEquals("Juan", response.username());
        assertEquals("Usuario registrado exitosamente", response.message());
    }

    // 5. authenticate - éxito
    @Test
    void authenticate_successful() {
        String email = "test@example.com";
        String rawPassword = "1234";
        String hashedPassword = "hashed1234";
        ZoneOffset offset = ZoneOffset.of("-05:00");

        Rol rol = Rol.builder()
                .id(1)
                .rol(RolEnum.USUARIO)
                .permisos(Set.of())
                .build();

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre("Test")
                .apellido("Example")
                .telefono("123456789")
                .dni(12345678)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .horasEstudioDia(2f)
                .horasExtracurricularDia(1f)
                .horasSuenoDia(7f)
                .horasSocialDia(1.5f)
                .horasActividadFisicaDia(1f)
                .promedioCalificaciones(9f)
                .build();

        Usuario userDetails = Usuario.builder()
                .email("test@example.com")
                .password("hashed1234")
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        Authentication auth = authService.authenticate(email, rawPassword);

        assertEquals(email, auth.getPrincipal());
    }

    // 6. authenticate - usuario no existe
    @Test
    void authenticate_userNotFound_throwsException() {
        String email = "notfound@example.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.authenticate(email, "any");
        });
    }

    // 7. authenticate - contraseña incorrecta
    @Test
    void authenticate_invalidPassword_throwsException() {
        String email = "user@example.com";
        String rawPassword = "wrong";
        String hashedPassword = "correct";
        ZoneOffset offset = ZoneOffset.of("-05:00");

        Rol rol = Rol.builder()
                .id(1)
                .rol(RolEnum.USUARIO)
                .permisos(Set.of())
                .build();

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre("Test")
                .apellido("Example")
                .telefono("123456789")
                .dni(12345678)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .horasEstudioDia(2f)
                .horasExtracurricularDia(1f)
                .horasSuenoDia(7f)
                .horasSocialDia(1.5f)
                .horasActividadFisicaDia(1f)
                .promedioCalificaciones(9f)
                .build();

        Usuario userDetails = Usuario.builder()
                .email("test@example.com")
                .password("correct")
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(userDetails));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticate(email, rawPassword);
        });
    }

    // 8. loadUserByUsername - éxito
    @Test
    void loadUserByUsername_successful() {
        ZoneOffset offset = ZoneOffset.of("-05:00");

        Rol rol = Rol.builder()
                .id(1)
                .rol(RolEnum.USUARIO)
                .permisos(Set.of())
                .build();

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre("Test")
                .apellido("Example")
                .telefono("123456789")
                .dni(12345678)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .horasEstudioDia(2f)
                .horasExtracurricularDia(1f)
                .horasSuenoDia(7f)
                .horasSocialDia(1.5f)
                .horasActividadFisicaDia(1f)
                .promedioCalificaciones(9f)
                .build();

        Usuario userDetails = Usuario.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userDetails));

        UserDetails loaded = authService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", loaded.getUsername());
    }

    // 9. loadUserByUsername - usuario no encontrado
    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        when(usuarioRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("missing@example.com");
        });
    }

    // 10. registerUser - validación de token correcto
    @Test
    void registerUser_generatesCorrectToken() {
        UsuarioFormRequest request = new UsuarioFormRequest(
                "Ana", "García", 87654321, LocalDate.of(1999, 5, 10), "987654321",
                "ana@example.com", "pass", 3f, 1f, 8f, 2f, 1f, 8.5f
        );

        Usuario mockUsuario = mock(Usuario.class);
        UsuarioInfo mockInfo = mock(UsuarioInfo.class);

        when(usuarioRegisterMapper.apply(request)).thenReturn(mockUsuario);
        when(usuarioRepository.save(mockUsuario)).thenReturn(mockUsuario);
        when(mockUsuario.getUsuarioInfo()).thenReturn(mockInfo);
        when(mockInfo.getNombre()).thenReturn("Ana");
        when(mockUsuario.getId()).thenReturn(7L);
//        when(mockUsuario.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));
        when(jwtService.generateToken(any(), eq(7L))).thenReturn("expected-token");

        AuthResponse response = authService.registerUser(request);

        assertEquals("expected-token", response.jwt());
    }
}

