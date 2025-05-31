package com.pgc.stress_predict.application.service;

import com.pgc.stress_predict.application.dto.request.AuthLoginRequest;
import com.pgc.stress_predict.application.dto.response.AuthResponse;
import com.pgc.stress_predict.application.dto.request.UsuarioFormRequest;
import com.pgc.stress_predict.application.mapper.UsuarioRegisterMapper;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.port.out.UsuarioRepository;
import com.pgc.stress_predict.infrastructure.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UsuarioRegisterMapper usuarioRegisterMapper;

    public AuthService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository,
                       JwtService jwtService, UsuarioRegisterMapper usuarioRegisterMapper) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.usuarioRegisterMapper = usuarioRegisterMapper;
    }

    // Auténtica y devuelve un AuthResponse si es que estamos logueados correctamente
    public AuthResponse loginUser(AuthLoginRequest userRequest) {
        String username = userRequest.email();
        String password = userRequest.password();

        Authentication auth = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Long usuarioId = usuarioRepository.findUsuarioByEmail(username);

        String accessToken = jwtService.generateToken(auth, usuarioId);

        return new AuthResponse(username, "Usuario logueado correctamente", accessToken, true);
    }

    @Transactional
    public AuthResponse registerUser(UsuarioFormRequest registerUserRequest) {
        String username = registerUserRequest.email();
        String password = registerUserRequest.contrasenia();

        Usuario usuarioCreado = usuarioRepository.save(usuarioRegisterMapper.apply(registerUserRequest));

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, usuarioCreado.getAuthorities());
        String accessToken = jwtService.generateToken(auth, usuarioCreado.getId());

        return new AuthResponse(usuarioCreado.getUsuarioInfo().getNombre(), "Usuario registrado exitosamente", accessToken, true);
    }

    // Autentica al usuario que quiere loguearse a la aplicacion
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Usuario invalido");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña invalida");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    // Busca el usuario en la base de datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con correo: " + username + " no existe"));
    }
}
