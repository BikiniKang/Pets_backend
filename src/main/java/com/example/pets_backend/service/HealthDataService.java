package com.example.pets_backend.service;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.health.WeightData;
import com.example.pets_backend.repository.PetRepository;
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

    private final PetRepository petRepository;
    private final WeightDataRepository weightDataRepo;
    private final CalorieDataRepository calorieDataRepo;
    private final SleepDataRepository sleepDataRepo;
    private final ExerciseDataRepository exerciseDataRepo;
    private final FoodDataRepository foodDataRepo;
    private final MediDataRepository mediDataRepo;

    public WeightData saveWeightData(WeightData weightData) {
        Pet pet = petRepository.findByPetId(weightData.getPet_id());
        weightData.setPet(pet);
        return weightDataRepo.save(weightData);
    }

    public void deleteWeightData (String data_id) {
        weightDataRepo.deleteById(data_id);
    }

}
