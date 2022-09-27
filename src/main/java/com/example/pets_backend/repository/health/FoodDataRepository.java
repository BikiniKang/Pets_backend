package com.example.pets_backend.repository.health;

import com.example.pets_backend.entity.health.FoodData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, String> {
}
