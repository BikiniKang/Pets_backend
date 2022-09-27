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
public class SleepData {
    @JsonIgnore
    @ManyToOne
    private Pet pet;

    @Id
    private String data_id = NanoIdUtils.randomNanoId();

    @NonNull
    private String pet_id;

    @NonNull
    @Column(length = 16)
    private String start_time;      // yyyy-MM-dd HH-mm

    @NonNull
    @Column(length = 16)
    private String end_time;        // yyyy-MM-dd HH-mm

    @Column(length = 5)
    private String duration_str;    // (H)H:mm

    private int minutes;
}
