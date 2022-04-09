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


//    @GetMapping("users")
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @GetMapping("users/{userId}/pets")
//    public List<Pet> getUserPets(@PathVariable Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new IllegalStateException("user with id " + userId + " does not exist")
//        );
//        return user.getPetList();
//    }
//
//    @PostMapping("users")
//    public User addUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }
//
//    @PostMapping("users/{userId}/addPet")
//    public Pet addPet(@PathVariable Long userId, @RequestBody Pet pet) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new IllegalStateException("user with id " + userId + " does not exist")
//        );
//        pet.setUser(user);
//        return petRepository.save(pet);
//    }
//
//    @DeleteMapping("users/{userId}")
//    public void deleteUser(@PathVariable Long userId) {
//        userRepository.deleteById(userId);
//    }
//
//    @Transactional
//    @PutMapping("users/{userId}")
//    public User changeUserName(@PathVariable Long userId, @RequestBody String userName) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new IllegalStateException("user with id " + userId + " does not exist")
//        );
//        user.setUserName(userName);
//        return user;
//    }

}
