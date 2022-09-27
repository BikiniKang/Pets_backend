package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class WeightData extends HealthData{

    private int weight;     // in kg


    public WeightData(String pet_id, String date, int weight) {
        super(pet_id, date);
        this.weight = weight;
    }
}
