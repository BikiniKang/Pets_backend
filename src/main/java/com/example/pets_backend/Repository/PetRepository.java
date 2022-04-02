package com.example.pets_backend.Repository;

import com.example.pets_backend.Entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
