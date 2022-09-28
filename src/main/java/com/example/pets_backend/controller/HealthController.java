package com.example.pets_backend.controller;

import com.example.pets_backend.entity.health.*;
import com.example.pets_backend.service.HealthDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Transactional
@Slf4j
public class HealthController {

    private final HealthDataService healthDataService;

    @DeleteMapping("/user/pet/health/delete")
    public void deleteData(@RequestParam String data_id) {
        healthDataService.deleteHealthData(data_id);
    }

    @PostMapping("/user/pet/weight/add")
    public HealthData addWeightData(@RequestBody WeightData weightData) {
        if (weightData.getWeight() == 0) {
            throw new IllegalArgumentException("Weight cannot be 0 or null");
        }
        // adding new WeightData to an existing date will automatically delete the previous one
        healthDataService.deleteSameDateData("WeightData", weightData.getDate());
        return healthDataService.saveHealthData(weightData);
    }

    @PostMapping("/user/pet/calorie/add")
    public HealthData addCalorieData(@RequestBody CalorieData calorieData) {
        if (calorieData.getCalorie() == 0) {
            throw new IllegalArgumentException("Calorie cannot be 0 or null");
        }
        // adding new CalorieData to an existing date will automatically delete the previous one
        healthDataService.deleteSameDateData("CalorieData", calorieData.getDate());
        return healthDataService.saveHealthData(calorieData);
    }

    @PostMapping("/user/pet/sleep/add")
    public HealthData addSleepData(@RequestBody SleepData sleepData) {
        if (sleepData.getDuration_str() == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        sleepData.setMinutes();
        // adding new SleepData to an existing date will automatically delete the previous one
        healthDataService.deleteSameDateData("SleepData", sleepData.getDate());
        return healthDataService.saveHealthData(sleepData);
    }

    @PostMapping("/user/pet/exercise/add")
    public HealthData addExerciseData(@RequestBody ExerciseData exerciseData) {
        if (exerciseData.getExercise_type() == null) {
            throw new IllegalArgumentException("Exercise type cannot be null");
        }
        if (exerciseData.getDuration_str() == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        exerciseData.setMinutes();
        // adding new ExerciseData to an existing date will automatically delete the previous one
        healthDataService.deleteSameDateData("ExerciseData", exerciseData.getDate());
        return healthDataService.saveHealthData(exerciseData);
    }

    @PostMapping("/user/pet/food/add")
    public HealthData addFoodData(@RequestBody FoodData foodData) {
        if (foodData.getFood_name() == null) {
            throw new IllegalArgumentException("Food name cannot be null");
        }
        if (foodData.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (foodData.getNotes() == null) {
            foodData.setNotes("NA");
        }
        return healthDataService.saveHealthData(foodData);
    }

    @PostMapping("/user/pet/medi/add")
    public HealthData addMediData(@RequestBody MediData mediData) {
        if (mediData.getMedi_name() == null) {
            throw new IllegalArgumentException("Medication name cannot be null");
        }
        if (mediData.getFrequency() == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }
        if (mediData.getNotes() == null) {
            mediData.setNotes("NA");
        }
        return healthDataService.saveHealthData(mediData);
    }


//    @PostMapping("/user/pet/weight")
//    public List<WeightData> getWeightList(@RequestParam String pet_id, @RequestParam String range) {
//        Pet pet = petService.findByPetId(pet_id);
//        return pet.getWeightListWithRange(range);
//    }


}
