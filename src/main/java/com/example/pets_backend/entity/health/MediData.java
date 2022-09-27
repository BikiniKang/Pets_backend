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
public class MediData {
    @JsonIgnore
    @ManyToOne
    private Pet pet;

    @Id
    private String data_id = NanoIdUtils.randomNanoId();

    @NonNull
    private String pet_id;

    @NonNull
    @Column(length = 64)
    private String medi_name;

    @NonNull
    @Column(length = 32)
    private String frequency;

    private String notes = "NA";
}
