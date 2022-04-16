package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepository extends JpaRepository<Species, Long> {
    Species findBySpeciesId(Long speciesId);
}
