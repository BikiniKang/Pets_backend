package com.example.pets_backend.entity.health;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class SleepData extends HealthData{

    @Column(length = 5)
    private String duration_str;    // (H)H:mm

    private int minutes;


    public SleepData(String pet_id, String date, String duration_str) {
        super(pet_id, date);
        this.duration_str = duration_str;
    }

    public void setMinutes() {
        String[] strings = this.duration_str.split(":");
        this.minutes = Integer.parseInt(strings[0])*60 + Integer.parseInt(strings[1]);
    }
}
