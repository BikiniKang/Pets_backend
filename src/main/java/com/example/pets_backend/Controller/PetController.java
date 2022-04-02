package com.example.pets_backend.Controller;

import com.example.pets_backend.Entity.Pet;
import com.example.pets_backend.Repository.PetRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping("pets")
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @DeleteMapping("pets/{petsId}")
    public void deletePet(@PathVariable Long petsId) {
        if (petRepository.findById(petsId).isPresent()) {
            petRepository.deleteById(petsId);
        } else {
            throw new IllegalStateException("pet with id " + petsId + " does not exist");
        }
    }

    @Transactional
    @PutMapping("pets/{petId}")
    public Pet changePetName(@PathVariable Long petId, @RequestBody String petName) {
        Pet pet = petRepository.findById(petId).orElseThrow(
                () -> new IllegalStateException("pet with id " + petId + " does not exist")
        );
        pet.setPetName(petName);
        return pet;
    }
}
