package com.example.pets_backend.util;


import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class SecurityHelperMethods {

    // 403
    public static void forbiddenErrorResponse(HttpServletResponse response, Exception exception) throws IOException {
        log.error("Error logging in: {}", exception.getMessage());
        response.setHeader("error", exception.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static String generateAccessToken(HttpServletRequest request, String username, String password) {
        return JWT.create()
                .withSubject(username)
                .withClaim("password", password)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .sign(ALGORITHM);
    }

    public static String generateRefreshToken(HttpServletRequest request, String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS_REFRESH))
                .sign(ALGORITHM);
    }
}
