package com.example.pets_backend.repository.health;

import com.example.pets_backend.entity.health.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseDataRepository extends JpaRepository<ExerciseData, String> {
}
