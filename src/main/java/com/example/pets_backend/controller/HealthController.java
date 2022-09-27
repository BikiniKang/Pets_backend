package com.example.pets_backend.controller;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.health.WeightData;
import com.example.pets_backend.service.HealthDataService;
import com.example.pets_backend.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class HealthController {

    private final HealthDataService healthDataService;
    private final PetService petService;

    @PostMapping("/user/pet/weight/add")
    public WeightData addWeightData(@RequestBody WeightData weightData) {
        return healthDataService.saveWeightData(weightData);
    }

    @DeleteMapping("/user/pet/weight/delete")
    public void deleteWeightData(@RequestParam String data_id) {
        healthDataService.deleteWeightData(data_id);
    }

    @PostMapping("/user/pet/weight")
    public List<WeightData> getWeightList(@RequestParam String pet_id, @RequestParam String range) {
        Pet pet = petService.findByPetId(pet_id);
        return pet.getWeightDataListWithRange(range);
    }
}
