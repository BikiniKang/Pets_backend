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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public User register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            return userService.register(user);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(400);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(400, "Duplicate email " + user.getEmail()));
            return null;
        }
    }

    @GetMapping(TOKEN_REFRESH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            String refresh_token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String email = decodedJWT.getSubject();
            String access_token_new = SecurityHelperMethods.generateAccessToken(request, email, userService.getUser(email).getPassword());
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token_new);
            tokens.put("refresh_token", refresh_token);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.success(tokens));
        } else {
            response.setStatus(400);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(400, "Refresh token is missing"));
        }
    }

    @GetMapping("/user")
    public void getUser(@RequestBody String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.getUser(email);
        response.setContentType(APPLICATION_JSON_VALUE);
        if (user == null) {
            response.setStatus(400);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(400, "User with email " + email + " not found in database"));
        } else {
            response.setStatus(200);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.success(user));
        }
    }

    @PutMapping("/user/edit_setting")
    @Transactional
    public void editUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user_old = userService.getUser(user.getEmail());
        response.setContentType(APPLICATION_JSON_VALUE);
        if (user_old == null) {
            response.setStatus(400);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(400, "User with email " + user.getEmail() + " not found in database"));
        } else {
            // Change the attributes which can be edited in the "Setting" page:
            user_old.setFirstName(user.getFirstName());
            user_old.setLastName(user.getLastName());
            user_old.setAddress(user.getAddress());
            user_old.setImage(user.getImage());
            user_old.setPhone(user.getPhone());
            response.setStatus(200);
            new ObjectMapper().writeValue(response.getOutputStream(), ResultData.success(user_old));
        }
    }

}
