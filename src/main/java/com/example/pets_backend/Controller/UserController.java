package com.example.pets_backend.Controller;

import com.example.pets_backend.Entity.User;
import com.example.pets_backend.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("users")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    @PutMapping("users/{userId}")
    public User changeUserName(@PathVariable Long userId, @RequestBody String userName) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("user with id " + userId + " does not exist")
        );
        user.setUserName(userName);
        return user;
    }

}
