package com.example.pets_backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import com.example.pets_backend.util.ResultData;
import com.example.pets_backend.util.SecurityHelperMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping(TOKEN_REFRESH)
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            // send the refresh token when the access token is expired
            String refresh_token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String email = decodedJWT.getSubject();
            String access_token_new = SecurityHelperMethods.generateAccessToken(request, email, userService.getUser(email).getPassword());
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token_new);
            tokens.put("refresh_token", refresh_token);
            return tokens;
        } else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(403);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(403, "Refresh token is missing"));
            return null;
        }
    }

    @GetMapping("/user")
    public User getUser(@RequestBody String email) {
        return userService.getUser(email);
    }

    @PutMapping("/user/edit_setting")
    public User editUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON_VALUE);

        // check whether the provided user matches the current user
        String email_provided = user.getEmail();
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length()); // no need to check the token format because the CustomAuthorizationFilter already did that
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        if (!email_provided.equals(decodedJWT.getSubject())) {
            throw new IllegalArgumentException("Do not have access to edit user " + email_provided);
        }

        return userService.editUser(user);
    }

}
