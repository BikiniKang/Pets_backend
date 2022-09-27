package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class MediData extends HealthData{

    private String medi_name;

    private String frequency;

    private String notes = "NA";

    public MediData(String pet_id, String date, String medi_name, String frequency, String notes) {
        super(pet_id, date);
        this.medi_name = medi_name;
        this.frequency = frequency;
        this.notes = notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
