package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinColumn(name = "uid", nullable = false)
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

    private int weight;

    private int height;

    @ManyToMany
    private List<Task> taskList = new ArrayList<>();

    @ManyToMany
    private List<Task> eventList = new ArrayList<>();
}
