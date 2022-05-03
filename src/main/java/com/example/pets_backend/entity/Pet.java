package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    private final String petId = NanoIdUtils.randomNanoId();

    @JsonIgnore
    @NonNull
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false, foreignKey = @ForeignKey(name = "fk_pet_uid"))
    private User user;

    @NonNull
    @Column(length = 32)
    private String petName;

    @NonNull
    private String petAvatar;

    @NonNull
    @Column(length = 32)
    private String species;

    @NonNull
    @Column(length = 32)
    private String breed;

    @NonNull
    private int gender;  // 0: female, 1: male, 2: N/A

    @NonNull
    @Column(length = 10)
    private String petDob; // "yyyy/mm/dd"

    private int weight; // in kg

    private int height; // in cm


    public LinkedHashMap<String, Object> getPetAb() {
        LinkedHashMap<String, Object> petAb = new LinkedHashMap<>();
        petAb.put("petId", this.petId);
        petAb.put("petName", this.petName);
        petAb.put("petAvatar", this.petAvatar);
        return petAb;
    }

}
