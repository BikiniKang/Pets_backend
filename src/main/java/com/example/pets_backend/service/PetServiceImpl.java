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
        log.info("New pet {} saved into database", pet.getPetName());
        return petRepository.save(pet);
    }

    @Override
    public Pet findByPetId(String petId) {
        Pet pet = petRepository.findByPetId(petId);
        if (pet == null) {
            log.error("Pet {} not found in the database", petId);
            throw new IllegalArgumentException("Pet " + petId + " not found in database");
        } else {
            log.error("Pet {} found in the database", petId);
        }
        return petRepository.findByPetId(petId);
    }

    @Override
    public void deleteByPetId(String petId) {
        Pet pet = petRepository.findByPetId(petId);
        if (pet == null) {
            log.error("Pet {} not found in the database", petId);
            throw new IllegalArgumentException("Pet " + petId + " not found in database");
        } else {
            log.error("Pet {} found in the database", petId);
        }
        petRepository.deleteById(petId);
    }
}
