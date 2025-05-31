package com.pgc.stress_predict.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${security.jwt.privateKey}")
    private String privateKey;

    @Value("${security.jwt.userGenerator}")
    private String userGenerator;

    public String generateToken(Authentication authentication, Long userId) {
        Algorithm algoritmo = Algorithm.HMAC256(privateKey);
        String usuarioAutenticado = authentication.getPrincipal().toString();
        String autorizaciones = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(usuarioAutenticado)
                .withClaim("authorities", autorizaciones)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algoritmo);
    }

    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algoritmo).build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("No se puede verificar el token");
        }
    }

    public String extractUsername(DecodedJWT decodedToken) {
        return decodedToken.getSubject();
    }

    public Claim getEspecificClaim(DecodedJWT decodedToken, String claimName) {
        return decodedToken.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedToken) {
        return decodedToken.getClaims();
    }
}
