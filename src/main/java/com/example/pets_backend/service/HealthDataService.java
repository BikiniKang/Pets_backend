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

}
