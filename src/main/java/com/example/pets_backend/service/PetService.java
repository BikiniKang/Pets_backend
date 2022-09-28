package com.example.pets_backend.service;


import com.example.pets_backend.entity.Pet;

public interface PetService {
    Pet save(Pet pet);
    Pet findByPetId(String petId);
    void deleteByPetId(String petId);
    void checkIfPetIdInDB(String petId);
}
