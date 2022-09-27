package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class FoodData extends HealthData{

    private String food_name;

    private String amount;

    private String notes = "NA";

    public FoodData(String pet_id, String date, String food_name, String amount, String notes) {
        super(pet_id, date);
        this.food_name = food_name;
        this.amount = amount;
        this.notes = notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
