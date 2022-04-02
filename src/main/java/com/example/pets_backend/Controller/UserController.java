package com.example.pets_backend.Controller;

import com.example.pets_backend.Entity.Pet;
import com.example.pets_backend.Entity.User;
import com.example.pets_backend.Repository.PetRepository;
import com.example.pets_backend.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public UserController(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("users/{userId}/pets")
    public List<Pet> getUserPets(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("user with id " + userId + " does not exist")
        );
        return user.getPetList();
    }

    @PostMapping("users")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("users/{userId}/addPet")
    public Pet addPet(@PathVariable Long userId, @RequestBody Pet pet) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("user with id " + userId + " does not exist")
        );
        pet.setUser(user);
        return petRepository.save(pet);
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
