package com.example.pets_backend.service;

import com.example.pets_backend.entity.health.HealthData;
import com.example.pets_backend.repository.health.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HealthDataService {

    private final PetService petService;
    private final HealthDataRepository healthDataRepo;


    public HealthData saveHealthData(HealthData healthData) {
        healthData.setPet(petService.findByPetId(healthData.getPet_id()));
        return healthDataRepo.save(healthData);
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
            default -> {}
        }
        if (sameDateData != null) {
            healthDataRepo.deleteById(sameDateData.getData_id());
        }
    }

}
