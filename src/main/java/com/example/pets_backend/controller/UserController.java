package com.example.pets_backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import com.example.pets_backend.util.SecurityHelperMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(REGISTER)
    public ResponseEntity<User> register(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(REGISTER).toUriString());
        return ResponseEntity.created(uri).body(userService.register(user));
    }

    @GetMapping(TOKEN_REFRESH)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            try {
                String refresh_token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
                JWTVerifier verifier = JWT.require(ALGORITHM).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String email = decodedJWT.getSubject();
                String access_token_new = SecurityHelperMethods.generateAccessToken(request, email, userService.getUser(email).getPassword());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token_new);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                SecurityHelperMethods.forbiddenErrorResponse(response, exception);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestBody String email) {
        return ResponseEntity.ok().body(userService.getUser(email));
    }

    @PutMapping("/user/edit_setting")
    @Transactional
    public ResponseEntity<User> editUser(@RequestBody User user) {
        User user_old = userService.getUser(user.getEmail());
        // Change the attributes which can be edited in the "Setting" page:
        user_old.setFirstName(user.getFirstName());
        user_old.setLastName(user.getLastName());
        user_old.setAddress(user.getAddress());
        user_old.setImage(user.getImage());
        user_old.setPhone(user.getPhone());
        return ResponseEntity.ok().body(user_old);
    }

}
