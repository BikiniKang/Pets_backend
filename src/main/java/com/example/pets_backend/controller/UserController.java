package com.example.pets_backend.controller;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.example.pets_backend.ConstantValues.REGISTER;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public ResponseEntity<User> register(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(REGISTER).toUriString());
        return ResponseEntity.created(uri).body(userService.register(user));
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
