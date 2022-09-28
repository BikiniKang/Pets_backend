package com.example.pets_backend.repository.health;


import com.example.pets_backend.entity.health.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, String> {

    @Query("SELECT w FROM WeightData w where w.date = ?1")
    WeightData findWeightDataByDate(String date);

    @Query("SELECT c FROM CalorieData c where c.date = ?1")
    CalorieData findCalorieDataByDate(String date);

    @Query("SELECT s FROM SleepData s where s.date = ?1")
    SleepData findSleepDataByDate(String date);

    @Query("SELECT e FROM ExerciseData e where e.date = ?1")
    ExerciseData findExerciseDataByDate(String date);
}
