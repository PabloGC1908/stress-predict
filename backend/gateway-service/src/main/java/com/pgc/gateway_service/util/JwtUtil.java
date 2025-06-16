package com.pgc.gateway_service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${security.jwt.privateKey}")
    private String privateKey;

    public void verifyToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algoritmo).build();
            verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("No se puede verificar el token");
        }
    }
}
