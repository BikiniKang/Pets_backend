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

//    @PostMapping("/user/pet/weight")
//    public List<WeightData> getWeightList(@RequestParam String pet_id, @RequestParam String range) {
//        Pet pet = petService.findByPetId(pet_id);
//        return pet.getWeightListWithRange(range);
//    }


}
