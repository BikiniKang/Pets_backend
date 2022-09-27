package com.example.pets_backend.entity.health;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class HealthData {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_health_pet_id"))
    private Pet pet;

    @Id
    private String data_id = NanoIdUtils.randomNanoId();;

    @Column(nullable = false)
    private String pet_id;

    @Column(length = 10, nullable = false)
    private String date;    // yyyy-MM-dd


    public HealthData(String pet_id, String date) {
        this.pet_id = pet_id;
        this.date = date;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
