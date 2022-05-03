package com.example.pets_backend.service;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PetServiceImpl implements PetService{

    private final PetRepository petRepository;

    @Override
    public Pet save(Pet pet) {
        log.info("Saved new pet with name {} into database", pet.getPetName());
        return petRepository.save(pet);
    }

    @Override
    public Pet findByPetId(String petId) {
        Pet pet = petRepository.findByPetId(petId);
        checkPetInDB(pet, petId);
        return pet;
    }

    @Override
    public void deleteByPetId(String petId) {
        Pet pet = petRepository.findByPetId(petId);
        checkPetInDB(pet, petId);
        petRepository.deleteById(petId);
    }

    private void checkPetInDB(Pet pet, String identifier) {
        if (pet == null) {
            log.error("Pet {} not found in the database", identifier);
            throw new IllegalArgumentException("Pet " + identifier + " not found in database");
        } else {
            log.info("Pet {} found in the database", identifier);
        }
    }
}
