package com.example.pets_backend.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.example.pets_backend.ConstantValues.*;

@Slf4j
public class SecurityHelperMethods {

    public static String generateAccessToken(HttpServletRequest request, String username, String password) {
        return JWT.create()
                .withSubject(username)
                .withClaim("password", password)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .sign(Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8)));
    }

    public static String generateRefreshToken(HttpServletRequest request, String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS_REFRESH))
                .sign(Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8)));
    }
}
