package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class MediData extends HealthData{

    @Column(nullable = false)
    private String medi_name;

    @Column(nullable = false)
    private String frequency;

    @Column(nullable = false)
    private String notes;

    public MediData(String pet_id, String uid, String date, String medi_name, String frequency, String notes) {
        super(pet_id, uid, date);
        this.medi_name = medi_name;
        this.frequency = frequency;
        this.notes = notes;
    }
}
