package com.example.pets_backend.service;


import com.example.pets_backend.entity.Pet;

public interface PetService {
    Pet save(Pet pet);
    Pet findByPetId(Long petId);
    void deletePetByPetId(Long petId);
}
