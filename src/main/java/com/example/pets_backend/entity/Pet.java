package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.pets_backend.ConstantValues.DEFAULT_IMAGE_PET;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

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

    private double weight;

    private double height;

    @ManyToMany
    private List<Task> taskList = new ArrayList<>();

    @ManyToMany
    private List<Task> eventList = new ArrayList<>();
}
