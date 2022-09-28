package com.example.pets_backend.controller;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.health.*;
import com.example.pets_backend.repository.PetRepository;
import com.example.pets_backend.repository.health.HealthDataRepository;
import com.example.pets_backend.service.HealthDataService;
import com.example.pets_backend.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Transactional
@Slf4j
public class HealthController {

    private final HealthDataService healthDataService;
    private final PetService petService;
    private final HealthDataRepository healthDataRepo;
    private final PetRepository petRepository;

    @DeleteMapping("/user/pet/health/delete")
    public void deleteData(@RequestParam String data_id) {
        healthDataService.deleteHealthData(data_id);
    }

    @PostMapping("/user/pet/weight/add")
    public HealthData addWeightData(@RequestBody WeightData weightData) {
        healthDataService.deleteSameDateData("WeightData", weightData.getDate());
        return healthDataService.saveWeightData(weightData);
    }

    @PostMapping("/user/pet/calorie/add")
    public HealthData addCalorieData(@RequestBody CalorieData calorieData) {
        healthDataService.deleteSameDateData("CalorieData", calorieData.getDate());
        return healthDataService.saveCalorieData(calorieData);
    }

    @PostMapping("/user/pet/sleep/add")
    public HealthData addSleepData(@RequestBody SleepData sleepData) {
        healthDataService.deleteSameDateData("SleepData", sleepData.getDate());
        return healthDataService.saveSleepData(sleepData);
    }

    @PostMapping("/user/pet/exercise/add")
    public HealthData addExerciseData(@RequestBody ExerciseData exerciseData) {
        healthDataService.deleteSameDateData("ExerciseData", exerciseData.getDate());
        return healthDataService.saveExerciseData(exerciseData);
    }

    @PostMapping("/user/pet/food/add")
    public HealthData addFoodData(@RequestBody FoodData foodData) {
        return healthDataService.saveFoodData(foodData);
    }

    @PostMapping("/user/pet/medi/add")
    public HealthData addMediData(@RequestBody MediData mediData) {
        return healthDataService.saveMediData(mediData);
    }

    @PostMapping("/user/pet/food/edit")
    public HealthData editFoodData (@RequestBody FoodData foodData) {
        return healthDataService.saveFoodData(foodData);
    }

    @PostMapping("/user/pet/medi/edit")
    public HealthData editMediData (@RequestBody MediData mediData) {
        return healthDataService.saveMediData(mediData);
    }

    @PostMapping("/user/pet/weight")
    public List<HealthData> getWeightDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "WeightData");
    }

    @PostMapping("/user/pet/calorie")
    public List<HealthData> getCalorieDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "CalorieData");
    }

    @PostMapping("/user/pet/sleep")
    public List<HealthData> getSleepDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "SleepData");
    }

    @PostMapping("/user/pet/exercise")
    public List<HealthData> getExerciseDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "ExerciseData");
    }

    @PostMapping("/user/pet/food")
    public List<HealthData> getFoodDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "FoodData");
    }

    @PostMapping("/user/pet/medi")
    public List<HealthData> getMediDataList (@RequestParam String pet_id, @RequestParam String range) {
        return healthDataService.getHealthData(pet_id, range, "MediData");
    }

    @PostMapping("/user/pet/health")
    public Map<String, List<HealthData>> getHealthDashboard (@RequestParam String pet_id, @RequestParam String range) {
        Pet pet = petService.findByPetId(pet_id);
        Map<String, List<HealthData>> map = new HashMap<>();
        map.put("weight_list", healthDataService.getHealthData(pet_id, range, "WeightData"));
        map.put("calorie_list", healthDataService.getHealthData(pet_id, range, "CalorieData"));
        map.put("sleep_list", healthDataService.getHealthData(pet_id, range, "SleepData"));
        map.put("exercise_list", healthDataService.getHealthData(pet_id, range, "ExerciseData"));
        map.put("food_list", healthDataService.getHealthData(pet_id, range, "FoodData"));
        map.put("medi_list", healthDataService.getHealthData(pet_id, range, "MediData"));
        return map;
    }
}
