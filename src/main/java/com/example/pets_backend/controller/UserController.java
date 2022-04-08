package com.example.pets_backend.controller;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("login")
    public String login(@RequestParam String email, @RequestParam String password) {
        //TODO: How to hide the password from the request URL?
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("user with email " + email + " does not exist")
        );
        if (user.getPassword().equals(password)) {
            return "";
        } else {
            throw new IllegalArgumentException("Entered wrong password");
        }
    }

    @PostMapping("register")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
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
