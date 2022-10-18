package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HealthThreshold {

    @Id
    private String pet_id;
    private int weight_min;
    private int weight_max;
    private int calorie_min;
    private int calorie_max;
    private int sleep_min;
    private int exercise_min;

    public HealthThreshold (String pet_id) {
        this.pet_id = pet_id;
    }

}
