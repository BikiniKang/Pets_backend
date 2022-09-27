package com.example.pets_backend.entity.health;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class WeightData {
    @JsonIgnore
    @ManyToOne
    private Pet pet;

    @Id
    private String data_id = NanoIdUtils.randomNanoId();

    @NonNull
    private String pet_id;

    @NonNull
    @Column(length = 10)
    private String date;    // yyyy-MM-dd

    @NonNull
    private int weight;     // in kg
}
