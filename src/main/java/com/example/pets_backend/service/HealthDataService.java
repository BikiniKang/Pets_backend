package com.example.pets_backend.service;

import com.example.pets_backend.entity.health.*;
import com.example.pets_backend.repository.health.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HealthDataService {

    private final PetService petService;
    private final HealthDataRepository healthDataRepo;

    public HealthData findById(String data_id) {
        Optional<HealthData> healthData = healthDataRepo.findById(data_id);
        if (healthData.isEmpty()) {
            throw new IllegalArgumentException("Data ID not found");
        }
        return healthData.get();
    }

    private HealthData saveHealthData(HealthData healthData) {
        healthData.setPet(petService.findByPetId(healthData.getPet_id()));
        return healthDataRepo.save(healthData);
    }

    public HealthData saveWeightData(WeightData weightData) {
        if (weightData.getWeight() == 0) {
            throw new IllegalArgumentException("Weight cannot be 0 or null");
        }
        return saveHealthData(weightData);
    }

    public HealthData saveCalorieData(CalorieData calorieData) {
        if (calorieData.getCalorie() == 0) {
            throw new IllegalArgumentException("Calorie cannot be 0 or null");
        }
        return saveHealthData(calorieData);
    }

    public HealthData saveSleepData (SleepData sleepData) {
        if (sleepData.getDuration_str() == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        sleepData.setMinutes();
        return saveHealthData(sleepData);
    }

    public HealthData saveExerciseData (ExerciseData exerciseData) {
        if (exerciseData.getExercise_type() == null) {
            throw new IllegalArgumentException("Exercise type cannot be null");
        }
        if (exerciseData.getDuration_str() == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        exerciseData.setMinutes();
        return saveHealthData(exerciseData);
    }

    public HealthData saveFoodData (FoodData foodData) {
        if (foodData.getFood_name() == null) {
            throw new IllegalArgumentException("Food name cannot be null");
        }
        if (foodData.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (foodData.getNotes() == null) {
            foodData.setNotes("NA");
        }
        return saveHealthData(foodData);
    }

    public HealthData saveMediData (MediData mediData) {
        if (mediData.getMedi_name() == null) {
            throw new IllegalArgumentException("Medication name cannot be null");
        }
        if (mediData.getFrequency() == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }
        if (mediData.getNotes() == null) {
            mediData.setNotes("NA");
        }
        return saveHealthData(mediData);
    }

    public void deleteHealthData(String data_id) {
        healthDataRepo.deleteById(data_id);
    }

    public void deleteSameDateData (String className, String date) {
        HealthData sameDateData = null;
        switch (className) {
            case "WeightData" -> sameDateData = healthDataRepo.findWeightDataByDate(date);
            case "CalorieData" -> sameDateData = healthDataRepo.findCalorieDataByDate(date);
            case "SleepData" -> sameDateData = healthDataRepo.findSleepDataByDate(date);
            case "ExerciseData" -> sameDateData = healthDataRepo.findExerciseDataByDate(date);
            default -> throw new IllegalArgumentException("className not recognized");
        }
        if (sameDateData != null) {
            healthDataRepo.deleteById(sameDateData.getData_id());
        }
    }

    public List<HealthData> getHealthData (String pet_id, String range, String className) {
        petService.checkIfPetIdInDB(pet_id);
        String startFrom = getDateStartFrom(range);
        return switch (className) {
            case "WeightData" -> healthDataRepo.getWeightData(pet_id, startFrom);
            case "CalorieData" -> healthDataRepo.getCalorieData(pet_id, startFrom);
            case "SleepData" -> healthDataRepo.getSleepData(pet_id, startFrom);
            case "ExerciseData" -> healthDataRepo.getExerciseData(pet_id, startFrom);
            case "FoodData" -> healthDataRepo.getFoodData(pet_id, startFrom);
            case "MediData" -> healthDataRepo.getMediData(pet_id, startFrom);
            default -> throw new IllegalArgumentException("className not recognized");
        };
    }

    private String getDateStartFrom (String range) {
        LocalDate today = LocalDate.now();
        return switch (range) {
            case "All" -> "2000-01-01";
            case "Week" -> today.minusDays(7).toString();
            case "Month" -> today.minusMonths(1).toString();
            case "6Month" -> today.minusMonths(6).toString();
            case "Year" -> today.minusYears(1).toString();
            default -> throw new IllegalArgumentException("Range not recognized");
        };
    }

}
