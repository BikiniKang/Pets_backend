package com.example.pets_backend.repository;

import com.example.pets_backend.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findByPetId(Long petId);
}
