package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class CalorieData extends HealthData{

    @Column(nullable = false)
    private int calorie;     // in Kcal


    public CalorieData(String pet_id, String date, int calorie) {
        super(pet_id, date);
        this.calorie = calorie;
    }
}
