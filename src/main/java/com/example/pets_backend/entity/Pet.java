package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.health.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    @Column(length = 32, nullable = false)
    private String petName;

    @NonNull
    private String petAvatar;

    @NonNull
    @Column(length = 32, nullable = false)
    private String species;

    @NonNull
    @Column(length = 32, nullable = false)
    private String breed;

    @NonNull
    @Column(nullable = false)
    private int gender;         // 0: female, 1: male, 2: N/A

    @NonNull
    @Column(length = 10, nullable = false)
    private String petDob;      // "yyyy-MM-dd"

    private int weight;         // in kg

    private int height;         // in cm

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<WeightData> weightDataList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<CalorieData> calorieDataList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<SleepData> sleepDataList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<ExerciseData> exerciseDataList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<FoodData> foodDataList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<MediData> mediDataList = new ArrayList<>();


    @JsonIgnore
    public LinkedHashMap<String, Object> getPetAb() {
        LinkedHashMap<String, Object> petAb = new LinkedHashMap<>();
        petAb.put("petId", this.petId);
        petAb.put("petName", this.petName);
        petAb.put("petAvatar", this.petAvatar);
        return petAb;
    }
}
